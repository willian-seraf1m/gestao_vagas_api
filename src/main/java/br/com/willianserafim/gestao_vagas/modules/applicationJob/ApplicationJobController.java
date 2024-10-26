package br.com.willianserafim.gestao_vagas.modules.applicationJob;

import br.com.willianserafim.gestao_vagas.modules.applicationJob.dto.ApplicationJobResponseDTO;
import br.com.willianserafim.gestao_vagas.modules.applicationJob.dto.CreateApplicationJobDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidate")
public class ApplicationJobController {

    @Autowired
    private ApplicationJobUseCase candidateApplicationJobUseCase;

    @PostMapping("/application")
    public ResponseEntity<Object> newApplyForJob(@RequestBody CreateApplicationJobDTO application) {
         try {
             return ResponseEntity.ok(this.candidateApplicationJobUseCase.newApplyForJob(application));
         } catch (Exception e) {
             return ResponseEntity.badRequest().body(e.getMessage());
         }
    }


    @GetMapping("/my-jobs-applications/{id}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public List<ApplicationJobResponseDTO> findJobApplicationByCandidateId(@PathVariable String id) {
        return this.candidateApplicationJobUseCase.findJobApplicationByCandidateId(id);
    }
}