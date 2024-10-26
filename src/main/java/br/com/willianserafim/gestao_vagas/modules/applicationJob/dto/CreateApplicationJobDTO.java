package br.com.willianserafim.gestao_vagas.modules.applicationJob.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateApplicationJobDTO {
    private UUID candidateId;
    private UUID jobId;
    private String status;
}
