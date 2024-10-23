package br.com.willianserafim.gestao_vagas.modules.company.useCases;

import br.com.willianserafim.gestao_vagas.modules.company.dto.JobConverterToDTO;
import br.com.willianserafim.gestao_vagas.modules.company.dto.JobDTO;
import br.com.willianserafim.gestao_vagas.modules.company.entities.JobEntity;
import br.com.willianserafim.gestao_vagas.modules.company.repositories.JobRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public JobUseCase(JobRepository jobRepository, JobConverterToDTO jobConverter) {
        this.jobRepository = jobRepository;
        this.jobConverter = jobConverter;
    }

    public List<JobDTO> findByCompanyId(UUID id) {
        return this.jobRepository.findByCompanyId(id)
                .stream()
                .map(jobConverter::convertToJobDTO)
                .collect(Collectors.toList());
    }

    public JobEntity createJob(JobEntity jobEntity) {
        return this.jobRepository.save(jobEntity);
    }

    public ResponseEntity<Object> updateJob(JobEntity updateData, HttpServletRequest request) throws AccessDeniedException {
        JobEntity job = jobRepository
                .findById(updateData.getId())
                .orElseThrow(EntityNotFoundException::new);

        var requestCompanyId = request.getAttribute("company_id");
        var jobCompanyId = job.getCompanyId().toString();


        if (!requestCompanyId.equals(jobCompanyId)) {
            throw new AccessDeniedException("You are not authorized to update this job.");
        }

        job.setDescription(updateData.getDescription());
        job.setLevel(updateData.getLevel());
        job.setBenefits(updateData.getBenefits());

        return ResponseEntity.ok(jobRepository.save(job));
    }

    public ResponseEntity<Object> deleteJob(UUID id, HttpServletRequest request) throws AccessDeniedException {
        JobEntity job = jobRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);

        var requestCompanyId = request.getAttribute("company_id");
        var jobCompanyId = job.getCompanyId().toString();


        if (!requestCompanyId.equals(jobCompanyId)) {
            throw new AccessDeniedException("You are not authorized to delete this job.");
        }

        jobRepository.delete(job);
        return ResponseEntity.noContent().build();
    }

}
