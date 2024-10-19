package br.com.willianserafim.gestao_vagas.modules.company.controllers;

import br.com.willianserafim.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.willianserafim.gestao_vagas.modules.company.useCases.CompanyUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyUseCase companyUseCase;

    @PostMapping("/register")
    public ResponseEntity<Object> createCompany(@Valid @RequestBody CompanyEntity companyEntity) {
        try {
            var result = this.companyUseCase.createCompany(companyEntity);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateCompany(@RequestBody CompanyEntity companyEntity, HttpServletRequest request) {
        try {
            var result = this.companyUseCase.updateCompany(companyEntity, request);
            return ResponseEntity.ok().body(result);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
