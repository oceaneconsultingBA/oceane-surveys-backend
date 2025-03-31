package com.oceane.surveys.controller;

import com.oceane.surveys.dto.QuestionDTO;
import com.oceane.surveys.services.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/surveys/{surveyId}/questions")
    public ResponseEntity<List<QuestionDTO>> getQuestionsBySurvey(@PathVariable Long surveyId) {
        return ResponseEntity.ok(questionService.getQuestionsBySurvey(surveyId));
    }

    @PostMapping("/surveys/{surveyId}/questions")
    public ResponseEntity<QuestionDTO> addQuestionToSurvey(
            @PathVariable Long surveyId,
            @Valid @RequestBody QuestionDTO questionDTO) {
        QuestionDTO createdQuestion = questionService.addQuestionToSurvey(surveyId, questionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody QuestionDTO questionDTO) {
        return ResponseEntity.ok(questionService.updateQuestion(id, questionDTO));
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
