package com.oceane.surveys.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oceane.surveys.dto.ApiResponse;
import com.oceane.surveys.dto.SurveyTokenDTO;
import com.oceane.surveys.mapper.SurveyMapper;
import com.oceane.surveys.services.SurveyTokenService;

@RestController
@RequestMapping("/api/survey-access")
public class SurveyAccessController {

    private final SurveyTokenService surveyTokenService;


    public SurveyAccessController(SurveyTokenService surveyTokenService, SurveyMapper surveyMapper) {
        this.surveyTokenService = surveyTokenService;
    }

    @GetMapping("/{token}")
    public ResponseEntity<ApiResponse<SurveyTokenDTO>> accessSurveyByToken(@PathVariable String token) {
        Optional<SurveyTokenDTO> tokenOpt = surveyTokenService.getSurveyTokenDTOByValidToken(token);

        if (tokenOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("Le token est expiré ou invalide."));
        }

        return ResponseEntity.ok(new ApiResponse<>(tokenOpt.get()));
    }

    @PatchMapping("/{token}/complete")
    public ResponseEntity<Void> completeSurvey(@PathVariable String token) {
        boolean updated = surveyTokenService.markTokenAsUsed(token);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
