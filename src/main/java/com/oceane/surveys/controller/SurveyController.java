package com.oceane.surveys.controller;

import com.oceane.surveys.dto.StatisticsDTO;
import com.oceane.surveys.dto.SurveyCreateDTO;
import com.oceane.surveys.dto.SurveyDTO;
import com.oceane.surveys.dto.TokenDTO;
import com.oceane.surveys.entities.SurveyStatus;
import com.oceane.surveys.services.SurveyService;
import com.oceane.surveys.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    private final SurveyService surveyService;
    private final TokenService tokenService;

    @Autowired
    public SurveyController(SurveyService surveyService, TokenService tokenService) {
        this.surveyService = surveyService;
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<List<SurveyDTO>> getAllSurveys(
            @RequestParam(required = false) SurveyStatus status,
            @RequestParam(required = false) String keyword) {

        if (status != null) {
            return ResponseEntity.ok(surveyService.findByStatus(status));
        } else if (keyword != null && !keyword.isEmpty()) {
            return ResponseEntity.ok(surveyService.searchByKeyword(keyword));
        } else {
            return ResponseEntity.ok(surveyService.getAllSurveys());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDTO> getSurveyById(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getSurveyById(id));
    }

    @GetMapping("/{id}/answers")
    public ResponseEntity<Map<Long, LocalDateTime>> getSurveyAnswerState(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getSurveyAnswerState(id));
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<TokenDTO> getSurveyByToken(@PathVariable String token) {
        return ResponseEntity.ok(tokenService.getToken(token));
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsDTO> getStatistics() {
        return ResponseEntity.ok(surveyService.getStatistics());
    }

    @PostMapping
    public ResponseEntity<SurveyDTO> createSurvey(@Valid @RequestBody SurveyCreateDTO createDTO) {
        SurveyDTO createdSurvey = surveyService.createSurvey(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSurvey);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurveyDTO> updateSurvey(
            @PathVariable Long id,
            @Valid @RequestBody SurveyDTO surveyDTO) {
        return ResponseEntity.ok(surveyService.updateSurvey(id, surveyDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SurveyDTO> changeSurveyStatus(
            @PathVariable Long id,
            @RequestParam SurveyStatus status) {
        return ResponseEntity.ok(surveyService.changeSurveyStatus(id, status));
    }
}
