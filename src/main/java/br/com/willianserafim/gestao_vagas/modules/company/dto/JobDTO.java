package br.com.willianserafim.gestao_vagas.modules.company.dto;

import br.com.willianserafim.gestao_vagas.modules.company.entities.JobEntity;
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
    private int numberJobApplications;
    private JobEntity.JobStatus status;
    private CompanyDTO company;
}
