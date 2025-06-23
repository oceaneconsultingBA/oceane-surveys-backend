package com.oceane.surveys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TokenDTO {
    private Long id;

    private String token;

    private SurveyDTO survey;

    private RecipientDTO recipient;
}
