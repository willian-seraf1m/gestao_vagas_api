package br.com.willianserafim.gestao_vagas.modules.candidate.controllers;

import br.com.willianserafim.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.willianserafim.gestao_vagas.modules.candidate.useCase.CandidateUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> updateCandidate(@RequestBody CandidateEntity candidateEntity) {
        try {
            var result = this.candidateUseCase.updateCandidate(candidateEntity);
            return ResponseEntity.ok().body(result);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
