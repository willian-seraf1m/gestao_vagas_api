package br.com.willianserafim.gestao_vagas.modules.company.useCase;

import br.com.willianserafim.gestao_vagas.exceptions.UserFoundException;
import br.com.willianserafim.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.willianserafim.gestao_vagas.modules.company.repositories.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CompanyUseCase {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CompanyEntity createCompany(CompanyEntity companyEntity) {
        this.companyRepository.
                findByUsernameOrEmail(companyEntity.getUsername(), companyEntity.getEmail()).
                ifPresent(user -> {
                   throw new UserFoundException();
                });

        var password = passwordEncoder.encode(companyEntity.getPassword());
        companyEntity.setPassword(password);

        return this.companyRepository.save(companyEntity);
    }

    public CompanyEntity updateCompany(CompanyEntity updatedData) {
        CompanyEntity userExists = companyRepository
                .findById(updatedData.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));

        companyRepository
                .findByUsernameOrEmail(updatedData.getUsername(), updatedData.getEmail())
                .ifPresent(user -> {
                    throw new UserFoundException();
                });

        userExists.setUsername(updatedData.getUsername());
        userExists.setEmail(updatedData.getEmail());
        userExists.setName(updatedData.getName());
        userExists.setWebsite(updatedData.getWebsite());
        userExists.setPassword(updatedData.getPassword());
        userExists.setDescription(updatedData.getDescription());

        return companyRepository.save(userExists);
    }


}
