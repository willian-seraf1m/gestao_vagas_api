package br.com.willianserafim.gestao_vagas.modules.publicInfo;

import br.com.willianserafim.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.willianserafim.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.willianserafim.gestao_vagas.modules.company.repositories.JobRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class PublicInfoUseCase {
    private final CandidateRepository candidateRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;

    public PublicInfoUseCase(CandidateRepository candidateRepository,
                             CompanyRepository companyRepository,
                             JobRepository jobRepository) {
        this.candidateRepository = candidateRepository;
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
    }

    public PublicInfoDTO getPublicInfo() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);

        long candidatesCount = candidateRepository.count();
        long companiesCount = companyRepository.count();
        long jobsCount = jobRepository.count();
        long jobsCreatedLastWeek = jobRepository.countByCreatedAtAfter(oneWeekAgo);

        return new PublicInfoDTO(candidatesCount, companiesCount, jobsCount, jobsCreatedLastWeek);
    }
}
