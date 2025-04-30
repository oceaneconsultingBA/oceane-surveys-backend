package com.oceane.surveys.controller;

import com.oceane.surveys.dto.StatisticsDTO;
import com.oceane.surveys.dto.SurveyCreateDTO;
import com.oceane.surveys.dto.SurveyDTO;
import com.oceane.surveys.entities.SurveyStatus;
import com.oceane.surveys.services.SurveyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
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
