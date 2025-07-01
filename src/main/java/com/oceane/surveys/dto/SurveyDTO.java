package com.oceane.surveys.dto;

import com.oceane.surveys.entities.SurveyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyDTO {
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 255, message = "Le titre ne peut pas dépasser 255 caractères")
    private String title;

    @Size(max = 1000, message = "La description ne peut pas dépasser 1000 caractères")
    private String description;

    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
    private SurveyStatus status;

    private List<QuestionDTO> questions = new ArrayList<>();
    private List<Long> recipientIds = new ArrayList<>();
}
