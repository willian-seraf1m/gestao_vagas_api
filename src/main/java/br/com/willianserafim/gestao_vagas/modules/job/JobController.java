package br.com.willianserafim.gestao_vagas.modules.job;

import br.com.willianserafim.gestao_vagas.modules.job.dto.CreateJobDTO;
import br.com.willianserafim.gestao_vagas.modules.job.dto.JobDTO;
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
@RequestMapping("/company")
@SecurityRequirement(name = "jwt_auth")
public class JobController {

    @Autowired
    private JobService jobService;
    private JobDTO jobDTO;

    @GetMapping("/my-jobs")
    @PreAuthorize("hasRole('COMPANY')")
    public List<JobDTO> findByCompanyId(HttpServletRequest request) {
        var companyId = UUID.fromString(request.getAttribute("company_id").toString());

        return this.jobService.findByCompanyId(companyId);
    }

    @PostMapping("/add-job")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Object> createJob(@Valid @RequestBody CreateJobDTO createJobDTO, HttpServletRequest request) {
        var companyId = request.getAttribute("company_id");

        var jobEntity = JobEntity.builder()
                .name(createJobDTO.getName())
                .companyId(UUID.fromString(companyId.toString()))
                .description(createJobDTO.getDescription())
                .level(createJobDTO.getLevel())
                .locality(createJobDTO.getLocality())
                .status(JobEntity.JobStatus.ACTIVE)
                .numberJobApplications(0)
                .build();
        try {
            var result = this.jobService.createJob(jobEntity);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update-job")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Object> updateJob(@Valid @RequestBody JobEntity jobEntity) {
        try {
            var result = this.jobService.updateJob(jobEntity);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/close-job/{id}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Object> closeJob(@PathVariable UUID id) {
        try {
            var result = this.jobService.closeJob(id);
            return ResponseEntity.ok(result);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
