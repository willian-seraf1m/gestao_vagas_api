package br.com.willianserafim.gestao_vagas.modules.job.dto;

import br.com.willianserafim.gestao_vagas.modules.company.dto.CompanyDTO;
import br.com.willianserafim.gestao_vagas.modules.company.CompanyEntity;
import br.com.willianserafim.gestao_vagas.modules.job.JobEntity;
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
                job.getLocality(),
                job.getNumberJobApplications(),
                job.getStatus(),
                convertToCompanyDTO(job.getCompanyEntity())
        );
    }
}
