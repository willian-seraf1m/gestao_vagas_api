package br.com.willianserafim.gestao_vagas.modules.company.dto;

import br.com.willianserafim.gestao_vagas.modules.company.entities.JobEntity;
import lombok.Data;

@Data
public class CreateJobDTO {
    private String name;
    private String description;
    private int numberJobApplications;
    private JobEntity.JobStatus status;
    private String level;
    private String locality;
}
