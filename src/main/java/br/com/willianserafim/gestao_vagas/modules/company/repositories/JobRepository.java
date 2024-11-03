package br.com.willianserafim.gestao_vagas.modules.company.repositories;

import br.com.willianserafim.gestao_vagas.modules.company.entities.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<JobEntity, UUID> {
    List<JobEntity> findByCompanyId(UUID companyId);
    List<JobEntity> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndLocality(String name, String description, String locality);
    long count();

    Long countByCreatedAtAfter(LocalDateTime startOfWeek);
}
