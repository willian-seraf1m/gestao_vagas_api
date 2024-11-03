package br.com.willianserafim.gestao_vagas.modules.company.useCases;

import br.com.willianserafim.gestao_vagas.modules.company.dto.JobConverterToDTO;
import br.com.willianserafim.gestao_vagas.modules.company.dto.JobDTO;
import br.com.willianserafim.gestao_vagas.modules.company.entities.JobEntity;
import br.com.willianserafim.gestao_vagas.modules.company.repositories.JobRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobUseCase {

    private final JobRepository jobRepository;
    private final JobConverterToDTO jobConverter;

    @Autowired
    JobConverterToDTO jobConverterToDTO;

    @Autowired
    public JobUseCase(JobRepository jobRepository, JobConverterToDTO jobConverter) {
        this.jobRepository = jobRepository;
        this.jobConverter = jobConverter;
    }

    private String getAuthenticatedCompanyId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public List<JobDTO> findByCompanyId(UUID id) {
        return this.jobRepository.findByCompanyId(id)
                .stream()
                .map(jobConverter::convertToJobDTO)
                .collect(Collectors.toList());
    }

    public JobEntity createJob(JobEntity jobEntity) {
        System.out.println(jobEntity.getStatus());
        return this.jobRepository.save(jobEntity);
    }

    public ResponseEntity<Object> updateJob(JobEntity updateData) throws AccessDeniedException {
        JobEntity job = jobRepository
                .findById(updateData.getId())
                .orElseThrow(EntityNotFoundException::new);

        var jobCompanyId = job.getCompanyId().toString();

        if (!getAuthenticatedCompanyId().equals(jobCompanyId)) {
            throw new AccessDeniedException("You are not authorized to update this job.");
        }

        job.setName(updateData.getName());
        job.setDescription(updateData.getDescription());
        job.setLevel(updateData.getLevel());

        JobDTO updatedJobDTO = jobConverterToDTO.convertToJobDTO(jobRepository.save(job));
        return ResponseEntity.ok(updatedJobDTO);
    }

    public ResponseEntity<Object> closeJob(UUID id) throws AccessDeniedException {
        JobEntity job = jobRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);

        String jobCompanyId = job.getCompanyId().toString();

        if (!getAuthenticatedCompanyId().equals(jobCompanyId)) {
            throw new AccessDeniedException("You are not authorized to close this job.");
        }

        if(job.getStatus().equals(JobEntity.JobStatus.CLOSED)) {
            return ResponseEntity.badRequest().body("This vacancy has now closed");
        }

        job.setStatus(JobEntity.JobStatus.CLOSED);
        jobRepository.save(job);

        return ResponseEntity.noContent().build();
    }
}
