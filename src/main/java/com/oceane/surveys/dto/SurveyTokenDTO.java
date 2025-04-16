package com.oceane.surveys.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SurveyTokenDTO {
    private SurveyDTO survey;
    private RecipientDTO recipient;
    private String errorMsg;

    public SurveyTokenDTO(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}


