package br.com.willianserafim.gestao_vagas.modules.applicationJob;

import br.com.willianserafim.gestao_vagas.exceptions.ApplicationJobFoundExeption;
import br.com.willianserafim.gestao_vagas.exceptions.JobClosedException;
import br.com.willianserafim.gestao_vagas.modules.applicationJob.dto.ApplicationJobResponseDTO;
import br.com.willianserafim.gestao_vagas.modules.applicationJob.dto.CreateApplicationJobDTO;
import br.com.willianserafim.gestao_vagas.modules.job.JobEntity;
import br.com.willianserafim.gestao_vagas.modules.job.JobRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ApplicationJobService {

    @Autowired
    private ApplicationJobRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Transactional
    public ApplicationJobEntity newApplyForJob(CreateApplicationJobDTO application) {
        JobEntity job = jobRepository.findById(application.getJobId())
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));

         boolean applicationExists = this.applicationRepository
                .findByJobIdAndCandidateId(application.getJobId(), application.getCandidateId())
                 .isPresent();

        if(applicationExists) {
            throw new ApplicationJobFoundExeption();
        }

        if(job.getStatus().equals(JobEntity.JobStatus.CLOSED)) {
            throw new JobClosedException();
        }

        job.setNumberJobApplications(job.getNumberJobApplications() + 1);
        jobRepository.save(job);

        ApplicationJobEntity applicationEntity = new ApplicationJobEntity(
                application.getCandidateId(),
                application.getJobId(),
                ApplicationJobEntity.ApplicationStatus.IN_PROGRESS
        );

        return this.applicationRepository.save(applicationEntity);
    }

    public List<ApplicationJobResponseDTO> findJobApplicationByCandidateId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID authenticatedUserId = UUID.fromString(authentication.getName());

        return this.applicationRepository.findByCandidateId(authenticatedUserId)
                .stream()
                .map(application ->  new ApplicationJobResponseDTO(
                        application.getId(),
                        application.getJobEntity().getName(),
                        application.getJobEntity().getCompanyEntity().getName(),
                        application.getStatus(),
                        application.getCreatedAt()
                )).collect(Collectors.toList());
    }
}
