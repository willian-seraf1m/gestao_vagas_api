package br.com.willianserafim.gestao_vagas.modules.applicationJob;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationJobRepository extends JpaRepository<ApplicationJobEntity, UUID> {
    List<ApplicationJobEntity> findByCandidateId(UUID id);
    Optional<ApplicationJobEntity> findByJobIdAndCandidateId(UUID jobId, UUID candidateId);
}
