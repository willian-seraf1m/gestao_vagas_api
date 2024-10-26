package br.com.willianserafim.gestao_vagas.modules.candidate;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "candidate")
public class CandidateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String name;

    @Pattern(regexp = "\\S+", message = "O campo (username) não deve conter espaços.")
    private String username;

    @Email(message = "O campo (email) deve conter um email válido.")
    private String email;

    @NotBlank
    @Size(min = 8, message = "A senha deve conter no pelo menos 8 caracteres")
    @Pattern(regexp = "\\S+", message = "A senha não pode conter espaços")
    private String password;

    @NotBlank
    private String description;

    private String curriculum;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
