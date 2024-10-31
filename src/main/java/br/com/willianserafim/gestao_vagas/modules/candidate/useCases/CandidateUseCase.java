package br.com.willianserafim.gestao_vagas.modules.candidate.useCases;

import br.com.willianserafim.gestao_vagas.exceptions.UserFoundException;
import br.com.willianserafim.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.willianserafim.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.willianserafim.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.willianserafim.gestao_vagas.modules.company.dto.JobConverterToDTO;
import br.com.willianserafim.gestao_vagas.modules.company.dto.JobDTO;
import br.com.willianserafim.gestao_vagas.modules.company.repositories.JobRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CandidateUseCase {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final JobRepository jobRepository;
    private final JobConverterToDTO jobConverter;

    @Autowired
    public CandidateUseCase(JobRepository jobRepository, JobConverterToDTO jobConverter) {
        this.jobRepository = jobRepository;
        this.jobConverter = jobConverter;
    }

    public ProfileCandidateResponseDTO getCandidateById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID authenticatedUserId = UUID.fromString(authentication.getName());

        var candidate = this.candidateRepository.findById(authenticatedUserId)
                .orElseThrow(EntityNotFoundException::new);

        ProfileCandidateResponseDTO candidateDTO =
                ProfileCandidateResponseDTO.builder()
                        .name(candidate.getName())
                        .username(candidate.getUsername())
                        .email(candidate.getEmail())
                        .id(candidate.getId())
                        .description(candidate.getDescription())
                        .build();

        return candidateDTO;
    }

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

    public CandidateEntity updateCandidate(CandidateEntity updatedData, HttpServletRequest request) throws AccessDeniedException {
        var candidateId = UUID.fromString(request.getAttribute("candidate_id").toString());

        CandidateEntity userExists = candidateRepository
                .findById(updatedData.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));

        if(!candidateId.equals(userExists.getId())) {
            throw new AccessDeniedException("You are not authorized to update this user.");
        }

        candidateRepository
                .findByUsernameOrEmail(updatedData.getUsername(), updatedData.getEmail())
                .ifPresent(user -> {
                    if (!user.getId().equals(updatedData.getId())) {
                        throw new UserFoundException();
                    }
                });

        Optional.ofNullable(updatedData.getUsername()).ifPresent(userExists::setUsername);
        Optional.ofNullable(updatedData.getEmail()).ifPresent(userExists::setEmail);
        Optional.ofNullable(updatedData.getName()).ifPresent(userExists::setName);
        Optional.ofNullable(updatedData.getPassword()).ifPresent(password ->
                userExists.setPassword(passwordEncoder.encode(password)));
        Optional.ofNullable(updatedData.getDescription()).ifPresent(userExists::setDescription);
        Optional.ofNullable(updatedData.getCurriculum()).ifPresent(userExists::setCurriculum);

        return candidateRepository.save(userExists);
    }

    public List<JobDTO> listAllJobsByFilter(String filter) {
        return this.jobRepository.findByDescriptionContainingIgnoreCase(filter)
                .stream()
                .map(jobConverter::convertToJobDTO)
                .collect(Collectors.toList());
    }
}
