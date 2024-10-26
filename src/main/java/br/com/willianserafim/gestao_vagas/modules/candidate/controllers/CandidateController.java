package br.com.willianserafim.gestao_vagas.modules.candidate.controllers;

import br.com.willianserafim.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.willianserafim.gestao_vagas.modules.candidate.useCases.CandidateUseCase;
import br.com.willianserafim.gestao_vagas.modules.company.dto.JobDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CandidateUseCase candidateUseCase;

    @PostMapping("/register")
    public ResponseEntity<Object> createCandidate(@Valid @RequestBody CandidateEntity candidateEntity) {
        try {
            var result = this.candidateUseCase.createCandidate(candidateEntity);
            return ResponseEntity.ok().body(result);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateCandidate(@RequestBody CandidateEntity candidateEntity, HttpServletRequest request) {
        try {
            var result = this.candidateUseCase.updateCandidate(candidateEntity, request);
            return ResponseEntity.ok().body(result);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Object> getCandidateById(HttpServletRequest request) {

        var idCandidate = request.getAttribute("candidate_id");

        try {
            var result = candidateUseCase.getCandidateById(UUID.fromString(idCandidate.toString()));
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/jobs")
    @PreAuthorize("hasRole('CANDIDATE')")
    @SecurityRequirement(name = "jwt_auth")
    public List<JobDTO> listAllJobsByFilter(@RequestParam String filter) {
        return this.candidateUseCase.listAllJobsByFilter(filter);
    }
}
