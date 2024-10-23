package br.com.willianserafim.gestao_vagas.modules.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class JobDTO {
    private UUID id;
    private String description;
    private String level;
    private String benefits;
    private CompanyDTO company;
}
