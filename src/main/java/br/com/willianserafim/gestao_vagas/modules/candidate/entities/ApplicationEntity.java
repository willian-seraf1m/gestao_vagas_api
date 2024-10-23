package br.com.willianserafim.gestao_vagas.modules.candidate.entities;

import br.com.willianserafim.gestao_vagas.modules.company.entities.JobEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "applications")
@Data
public class ApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "candidate_id", insertable = false, updatable = false)
    private  CandidateEntity candidateEntity;

    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @OneToOne
    @JoinColumn(name = "job_id", insertable = false, updatable = false)
    private JobEntity jobEntity;

    @Column(name = "job_id", nullable = false)
    private UUID jobId;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum ApplicationStatus {
        IN_PROGRESS,
        FINISHED
    }
}
