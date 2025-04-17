package com.oceane.surveys.dto;

import com.oceane.surveys.entities.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;

    @NotBlank(message = "Le texte de la question est obligatoire")
    private String text;

    @NotNull(message = "Le type de question est obligatoire")
    private QuestionType type;

    private boolean required;
    private int displayOrder;
    private String conditionalLogic;

    private List<QuestionOptionDTO> options = new ArrayList<>();

}
