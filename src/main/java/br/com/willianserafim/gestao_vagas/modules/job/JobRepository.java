package br.com.willianserafim.gestao_vagas.modules.job;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<JobEntity, UUID> {
    List<JobEntity> findByCompanyId(UUID companyId);
    List<JobEntity> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndLocality(String name, String description, String locality);
    long count();

    Long countByCreatedAtAfter(LocalDateTime startOfWeek);
}
