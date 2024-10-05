package br.com.willianserafim.gestao_vagas.modules.company.controllers;

import br.com.willianserafim.gestao_vagas.modules.company.entities.JobEntity;
import br.com.willianserafim.gestao_vagas.modules.company.useCase.JobUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobUseCase jobUseCase;

    @PostMapping("/add")
    public ResponseEntity<Object> create(@Valid @RequestBody JobEntity jobEntity) {
        try {
            var result = this.jobUseCase.createJob(jobEntity);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateJob(@Valid @RequestBody JobEntity jobEntity) {
        try {
            var result = this.jobUseCase.updateJob(jobEntity);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Object> removeJob(@PathVariable UUID id) {
        try {
            var result = this.jobUseCase.deleteJob(id);
            return ResponseEntity.ok(result);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
