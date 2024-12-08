package br.com.willianserafim.gestao_vagas.modules.candidate.services;

import br.com.willianserafim.gestao_vagas.aws.S3Service;
import br.com.willianserafim.gestao_vagas.exceptions.UserFoundException;
import br.com.willianserafim.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.willianserafim.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.willianserafim.gestao_vagas.modules.candidate.dto.CandidateDTO;
import br.com.willianserafim.gestao_vagas.modules.job.dto.JobConverterToDTO;
import br.com.willianserafim.gestao_vagas.modules.job.dto.JobDTO;
import br.com.willianserafim.gestao_vagas.modules.job.JobRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final JobRepository jobRepository;
    private final JobConverterToDTO jobConverter;
    private final S3Service s3Service;

    @Autowired
    public CandidateService(JobRepository jobRepository, JobConverterToDTO jobConverter, S3Service s3Service) {
        this.jobRepository = jobRepository;
        this.jobConverter = jobConverter;
        this.s3Service = s3Service;
    }

    public String uploadCurriculum(UUID candidateId, MultipartFile file) throws IOException {
        CandidateEntity candidate = candidateRepository.findById(candidateId)
                .orElseThrow(UserFoundException::new);

        String fileUrl = s3Service.uploadFile (file);

        candidate.setCurriculumUrl(fileUrl);
        candidateRepository.save(candidate);

        return fileUrl;
    }

    public ResponseInputStream<GetObjectResponse> downloadCurriculum(UUID candidateId) {
        CandidateEntity candidate = candidateRepository.findById(candidateId)
                .orElseThrow(UserFoundException::new);

        if (candidate.getCurriculumUrl() == null) {
            throw new RuntimeException("Curriculum not found");
        }

        String fileName = candidate.getCurriculumUrl().substring(candidate.getCurriculumUrl().lastIndexOf("/")+1);
        return s3Service.downloadFile(fileName);
    }

    public CandidateDTO getCandidateById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID authenticatedUserId = UUID.fromString(authentication.getName());

        var candidate = this.candidateRepository.findById(authenticatedUserId)
                .orElseThrow(EntityNotFoundException::new);

        CandidateDTO candidateDTO =
                CandidateDTO.builder()
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
        Optional.ofNullable(updatedData.getCurriculumUrl()).ifPresent(userExists::setCurriculumUrl);

        return candidateRepository.save(userExists);
    }

    public List<JobDTO> listAllJobsByFilter(String name, String description, String locality) {
        return this.jobRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndLocality(name, description, locality)
                .stream()
                .map(jobConverter::convertToJobDTO)
                .collect(Collectors.toList());
    }

    public JobDTO findJobById(UUID id) {
        return this.jobRepository.findById(id)
                .map(jobConverter::convertToJobDTO)
                .orElseThrow(() -> new EntityNotFoundException(("job not found with id:" + id)));
    }
}
