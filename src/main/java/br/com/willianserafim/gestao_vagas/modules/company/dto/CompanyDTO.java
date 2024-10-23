package br.com.willianserafim.gestao_vagas.modules.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyDTO {
    private String name;
    private String username;
    private String website;
    private String email;

}


