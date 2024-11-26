package br.com.willianserafim.gestao_vagas.modules.company.services;

import br.com.willianserafim.gestao_vagas.aws.S3Service;
import br.com.willianserafim.gestao_vagas.exceptions.UserFoundException;
import br.com.willianserafim.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.willianserafim.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.willianserafim.gestao_vagas.modules.company.CompanyEntity;
import br.com.willianserafim.gestao_vagas.modules.company.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private  final PasswordEncoder passwordEncoder;

    public CompanyService(CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

    public CompanyEntity findCompanyById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID authenticatedCompanyId = UUID.fromString(authentication.getName());

        return this.companyRepository.findById(authenticatedCompanyId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public CompanyEntity updateCompany(CompanyEntity updatedData) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID authenticatedCompanyId = UUID.fromString(authentication.getName());

        CompanyEntity companyExists = companyRepository
                .findById(updatedData.getId())
                .orElseThrow(() -> new EntityNotFoundException("Company not found!"));

        if(!authenticatedCompanyId.equals(companyExists.getId())) {
            throw new AccessDeniedException("You are not authorized to update this company.");
        }

        companyRepository
                .findByUsernameOrEmail(updatedData.getUsername(), updatedData.getEmail())
                .ifPresent(user -> {
                    if (!user.getId().equals(updatedData.getId())) {
                        throw new UserFoundException();
                    }
                });

        Optional.ofNullable(updatedData.getName()).ifPresent(companyExists::setName);
        Optional.ofNullable(updatedData.getUsername()).ifPresent(companyExists::setUsername);
        Optional.ofNullable(updatedData.getEmail()).ifPresent(companyExists::setEmail);
        Optional.ofNullable(updatedData.getPassword()).ifPresent(password ->
                companyExists.setPassword(passwordEncoder.encode(password)));
        Optional.ofNullable(updatedData.getWebsite()).ifPresent(companyExists::setWebsite);
        Optional.ofNullable(updatedData.getDescription()).ifPresent(companyExists::setDescription);

        return companyRepository.save(companyExists);
    }
}
