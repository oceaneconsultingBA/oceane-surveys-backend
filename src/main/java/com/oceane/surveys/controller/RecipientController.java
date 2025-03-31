package com.oceane.surveys.controller;

import com.oceane.surveys.dto.RecipientDTO;
import com.oceane.surveys.services.RecipientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipients")
public class RecipientController {

    private final RecipientService recipientService;

    @Autowired
    public RecipientController(RecipientService recipientService) {
        this.recipientService = recipientService;
    }

    @GetMapping
    public ResponseEntity<List<RecipientDTO>> getAllRecipients() {
        return ResponseEntity.ok(recipientService.getAllRecipients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipientDTO> getRecipientById(@PathVariable Long id) {
        return ResponseEntity.ok(recipientService.getRecipientById(id));
    }

    @PostMapping
    public ResponseEntity<RecipientDTO> createRecipient(@Valid @RequestBody RecipientDTO recipientDTO) {
        RecipientDTO createdRecipient = recipientService.createRecipient(recipientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipientDTO> updateRecipient(
            @PathVariable Long id,
            @Valid @RequestBody RecipientDTO recipientDTO) {
        return ResponseEntity.ok(recipientService.updateRecipient(id, recipientDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipient(@PathVariable Long id) {
        recipientService.deleteRecipient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<List<RecipientDTO>> getRecipientsBySurvey(@PathVariable Long surveyId) {
        return ResponseEntity.ok(recipientService.getRecipientsBySurvey(surveyId));
    }

    @PostMapping("/{recipientId}/surveys/{surveyId}")
    public ResponseEntity<Void> addRecipientToSurvey(
            @PathVariable Long recipientId,
            @PathVariable Long surveyId) {
        recipientService.addRecipientToSurvey(surveyId, recipientId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{recipientId}/surveys/{surveyId}")
    public ResponseEntity<Void> removeRecipientFromSurvey(
            @PathVariable Long recipientId,
            @PathVariable Long surveyId) {
        recipientService.removeRecipientFromSurvey(surveyId, recipientId);
        return ResponseEntity.noContent().build();
    }
}
