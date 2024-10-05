package br.com.willianserafim.gestao_vagas.modules.company.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "company")
@Data
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    @Pattern(regexp = "\\S+", message = "O campo (username) não deve conter espaços.")
    private String username;

    @Email(message = "O campo (email) deve conter um email válido.")
    private String email;

    @Size(min = 8, message = "A senha deve conter pelo menos 8 caracteres")
    private String password;

    private String website;
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
