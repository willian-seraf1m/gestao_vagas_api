package br.com.willianserafim.gestao_vagas.modules.applicationJob;

import br.com.willianserafim.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.willianserafim.gestao_vagas.modules.company.entities.JobEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "application_job")
@Data
@NoArgsConstructor
public class ApplicationJobEntity {

    public ApplicationJobEntity(UUID candidateId, UUID jobId, String status) {
        this.candidateId = candidateId;
        this.jobId = jobId;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "candidate_id", insertable = false, updatable = false)
    private CandidateEntity candidateEntity;

    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @OneToOne
    @JoinColumn(name = "job_id", insertable = false, updatable = false)
    private JobEntity jobEntity;

    @Column(name = "job_id", nullable = false)
    private UUID jobId;

    @NotBlank(message = "Status não pode ser vazio")
    @Size(max = 15)
    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
