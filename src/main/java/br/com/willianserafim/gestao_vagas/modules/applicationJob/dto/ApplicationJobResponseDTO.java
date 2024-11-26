package br.com.willianserafim.gestao_vagas.modules.applicationJob.dto;

import br.com.willianserafim.gestao_vagas.modules.applicationJob.ApplicationJobEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ApplicationJobResponseDTO {
    private UUID applicationJobId;
    private String jobName;
    private String companyName;
    private ApplicationJobEntity.ApplicationStatus status;
    private LocalDateTime createdAt;
}
