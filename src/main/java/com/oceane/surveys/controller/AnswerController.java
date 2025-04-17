package com.oceane.surveys.controller;

import com.oceane.surveys.dto.AnswerDTO;
import com.oceane.surveys.services.AnswerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {
    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping("{surveyId}")
    public ResponseEntity<List<AnswerDTO>> getQuestionsBySurvey(@PathVariable Long surveyId) {
        return ResponseEntity.ok(answerService.getAnswersBySurvey(surveyId));
    }

    @GetMapping("{surveyId}/recipient/{recipientId}")
    public ResponseEntity<List<AnswerDTO>> getQuestionsBySurveyAndRecipient(@PathVariable Long surveyId, @PathVariable Long recipientId) {
        return ResponseEntity.ok(answerService.getAnswersBySurveyAndRecipient(surveyId, recipientId));
    }

    @PostMapping("{surveyId}")
    public ResponseEntity<Map<Long, AnswerDTO>> saveAnswers(
            @PathVariable Long surveyId,
            @Valid @RequestBody Map<Long, AnswerDTO> answerDTOsByQuestionId) {
        Map<Long, AnswerDTO> createdQuestion = answerService.saveAnswers(surveyId, answerDTOsByQuestionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }
}
