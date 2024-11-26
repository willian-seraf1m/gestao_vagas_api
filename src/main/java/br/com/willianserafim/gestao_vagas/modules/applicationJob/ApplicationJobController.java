package br.com.willianserafim.gestao_vagas.modules.applicationJob;

import br.com.willianserafim.gestao_vagas.modules.applicationJob.dto.CreateApplicationJobDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidate")
@SecurityRequirement(name = "jwt_token")
public class ApplicationJobController {

    @Autowired
    private ApplicationJobService applicationJobService;

    @PostMapping("/application")
    public ResponseEntity<Object> newApplyForJob(@RequestBody CreateApplicationJobDTO application) {
         try {
             return ResponseEntity.ok(this.applicationJobService.newApplyForJob(application));
         } catch (Exception e) {
             return ResponseEntity.badRequest().body(e.getMessage());
         }
    }


    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Object> findJobApplicationByCandidateId() {
        return ResponseEntity.ok().body(this.applicationJobService.findJobApplicationByCandidateId());
    }
}
