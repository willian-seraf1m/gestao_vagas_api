package br.com.willianserafim.gestao_vagas.modules.company.dto;

import br.com.willianserafim.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.willianserafim.gestao_vagas.modules.company.entities.JobEntity;
import org.springframework.stereotype.Component;

@Component
public class JobConverterToDTO {
    public CompanyDTO convertToCompanyDTO(CompanyEntity company) {
        return new CompanyDTO(
                company.getName(),
                company.getUsername(),
                company.getWebsite(),
                company.getEmail()
        );
    }

    public JobDTO convertToJobDTO(JobEntity job) {
        return new JobDTO(
                job.getId(),
                job.getName(),
                job.getDescription(),
                job.getLevel(),
                job.getBenefits(),
                convertToCompanyDTO(job.getCompanyEntity())
        );
    }
}
