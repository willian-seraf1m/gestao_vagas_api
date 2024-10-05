package br.com.willianserafim.gestao_vagas.modules.candidate.useCase;

import br.com.willianserafim.gestao_vagas.exceptions.UserFoundException;
import br.com.willianserafim.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.willianserafim.gestao_vagas.modules.candidate.CandidateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CandidateUseCase {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CandidateEntity createCandidate(CandidateEntity candidateEntity) {
        this.candidateRepository.
                findByUsernameOrEmail(candidateEntity.getUsername(), candidateEntity.getEmail()).
                ifPresent((user) -> {
                    throw new UserFoundException();
                });

        var password = passwordEncoder.encode(candidateEntity.getPassword());
        candidateEntity.setPassword(password);

        return this.candidateRepository.save(candidateEntity);
    }

    public CandidateEntity updateCandidate(CandidateEntity updatedData) {
        CandidateEntity userExists = candidateRepository
                .findById(updatedData.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));

        candidateRepository
                .findByUsernameOrEmail(updatedData.getUsername(), updatedData.getEmail())
                .ifPresent(user -> {
                    throw new UserFoundException();
                });

        userExists.setUsername(updatedData.getUsername());
        userExists.setEmail(updatedData.getEmail());
        userExists.setName(updatedData.getName());
        userExists.setPassword(updatedData.getPassword());
        userExists.setDescription(updatedData.getDescription());
        userExists.setCurriculum(updatedData.getCurriculum());

        return candidateRepository.save(userExists);
    }
}
