package com.oceane.surveys.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOptionDTO {
    private Long id;

    @NotBlank(message = "Le texte de l'option est obligatoire")
    private String text;

    private int displayOrder;
}
