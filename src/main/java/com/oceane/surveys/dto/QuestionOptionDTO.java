package com.oceane.surveys.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOptionDTO {
    private Long id;

    @NotBlank(message = "Le texte de l'option est obligatoire")
    private String text;

    private int displayOrder;
}
