package br.com.willianserafim.gestao_vagas.modules.candidate.controllers;

import br.com.willianserafim.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.willianserafim.gestao_vagas.modules.candidate.services.CandidateService;
import br.com.willianserafim.gestao_vagas.modules.job.dto.JobDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @PostMapping("/{id}/upload-curriculum")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Object> uploadCurriculum(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        UUID candidateId = UUID.fromString(id);

        try {
            String fileUrl = candidateService.uploadCurriculum(candidateId, file);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error when trying to upload: "+ e.getMessage());
        }
    }

    @GetMapping("/{id}/download-curriculum")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Object> downloadCurriculum(@PathVariable String id) {
        UUID candidateId = UUID.fromString(id);

        try {
            ResponseInputStream<GetObjectResponse> fileStream = candidateService.downloadCurriculum(candidateId);
            byte[] fileBytes = fileStream.readAllBytes();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resume_"+id+".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(fileBytes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error when trying to download: "+ e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> createCandidate(@Valid @RequestBody CandidateEntity candidateEntity) {
        try {
            var result = this.candidateService.createCandidate(candidateEntity);
            return ResponseEntity.ok().body(result);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('CANDIDATE')")
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> updateCandidate(@RequestBody CandidateEntity candidateEntity, HttpServletRequest request) {
        try {
            var result = this.candidateService.updateCandidate(candidateEntity, request);
            return ResponseEntity.ok().body(result);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> getCandidateById() {
        try {
            var result = candidateService.getCandidateById();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/jobs")
    @PreAuthorize("hasRole('CANDIDATE')")
    @SecurityRequirement(name = "jwt_auth")
    public List<JobDTO> listAllJobsByFilter(@RequestParam String filter, @RequestParam String locality) {
        return this.candidateService.listAllJobsByFilter(filter, filter, locality);
    }

    @GetMapping("/job/{id}")
    @PreAuthorize("hasRole('CANDIDATE')")
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> findJobById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(this.candidateService.findJobById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
