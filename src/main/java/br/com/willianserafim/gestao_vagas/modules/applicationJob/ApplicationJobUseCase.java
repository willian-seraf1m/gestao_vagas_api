package br.com.willianserafim.gestao_vagas.modules.applicationJob;

import br.com.willianserafim.gestao_vagas.exceptions.ApplicationJobFoundExeption;
import br.com.willianserafim.gestao_vagas.modules.applicationJob.dto.ApplicationJobResponseDTO;
import br.com.willianserafim.gestao_vagas.modules.applicationJob.dto.CreateApplicationJobDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ApplicationJobUseCase {

    @Autowired
    private ApplicationJobRepository applicationRepository;

    public ApplicationJobEntity newApplyForJob(CreateApplicationJobDTO application) {
         Boolean applicationExisists = this.applicationRepository
                .findByJobIdAndCandidateId(application.getJobId(), application.getCandidateId())
                 .isPresent();

        if(applicationExisists) {
            throw new ApplicationJobFoundExeption();
        }

        ApplicationJobEntity applicationEntity = new ApplicationJobEntity(
                application.getCandidateId(),
                application.getJobId(),
                application.getStatus()
        );

        return this.applicationRepository.save(applicationEntity);
    }

    public List<ApplicationJobResponseDTO> findJobApplicationByCandidateId(String id) {
        UUID candidateId = UUID.fromString(id);

        List<ApplicationJobResponseDTO> applications = this.applicationRepository.findByCandidateId(candidateId)
                .stream()
                .map(application -> {
                    return new ApplicationJobResponseDTO(
                            application.getId(),
                            application.getCandidateId(),
                            application.getJobId(),
                            application.getStatus(),
                            application.getCreatedAt()
                    );
                }).collect(Collectors.toList());

        return applications;
    }
}
