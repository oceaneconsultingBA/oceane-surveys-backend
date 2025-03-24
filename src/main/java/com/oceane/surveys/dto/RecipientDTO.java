package com.oceane.surveys.dto;

import com.oceane.surveys.entities.RecipientType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipientDTO {
    private Long id;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    private String firstName;
    private String lastName;
    private String company;

    @NotNull(message = "Le type de destinataire est obligatoire")
    private RecipientType type;
}
