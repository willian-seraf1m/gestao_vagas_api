package br.com.willianserafim.gestao_vagas.modules.company.repositories;

import br.com.willianserafim.gestao_vagas.modules.company.entities.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<JobEntity, UUID> {
}
