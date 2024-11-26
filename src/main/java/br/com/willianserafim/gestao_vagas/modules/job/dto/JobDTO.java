package br.com.willianserafim.gestao_vagas.modules.job.dto;

import br.com.willianserafim.gestao_vagas.modules.company.dto.CompanyDTO;
import br.com.willianserafim.gestao_vagas.modules.job.JobEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class JobDTO {
    private UUID id;
    private String name;
    private String description;
    private String level;
    private String locality;
    private int numberJobApplications;
    private JobEntity.JobStatus status;
    private CompanyDTO company;
}
