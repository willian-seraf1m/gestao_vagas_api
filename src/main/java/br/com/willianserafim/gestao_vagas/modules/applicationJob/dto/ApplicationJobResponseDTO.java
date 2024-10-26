package br.com.willianserafim.gestao_vagas.modules.applicationJob.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ApplicationJobResponseDTO {
    private UUID applicationJobId;
    private UUID candidateId;
    private UUID jobId;
    private String status;
    private LocalDateTime createdAt;
}
