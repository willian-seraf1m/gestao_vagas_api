package br.com.willianserafim.gestao_vagas.modules.company.useCase;

import br.com.willianserafim.gestao_vagas.modules.company.entities.JobEntity;
import br.com.willianserafim.gestao_vagas.modules.company.repositories.JobRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class JobUseCase {

    @Autowired
    private JobRepository jobRepository;

    public JobEntity createJob(JobEntity jobEntity) {
        return this.jobRepository.save(jobEntity);
    }

    public JobEntity updateJob(JobEntity updateData) {
        JobEntity job = jobRepository
                .findById(updateData.getId())
                .orElseThrow(EntityNotFoundException::new);

        job.setDescription(updateData.getDescription());
        job.setLevel(updateData.getLevel());
        job.setBenefits(updateData.getBenefits());

        return jobRepository.save(job);
    }

    public ResponseEntity<Object> deleteJob(UUID id) {
        JobEntity job = jobRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);

        jobRepository.delete(job);
        return ResponseEntity.noContent().build();
    }

}
