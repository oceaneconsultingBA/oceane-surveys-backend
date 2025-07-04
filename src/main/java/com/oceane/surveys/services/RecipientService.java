package com.oceane.surveys.services;

import com.oceane.surveys.dto.RecipientDTO;
import com.oceane.surveys.entities.Recipient;
import com.oceane.surveys.entities.Survey;
import com.oceane.surveys.exception.ResourceNotFoundException;
import com.oceane.surveys.mapper.SurveyMapper;
import com.oceane.surveys.repositories.RecipientRepository;
import com.oceane.surveys.repositories.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecipientService {
    private final SurveyMapper surveyMapper;
    private final RecipientRepository recipientRepository;
    private final SurveyRepository surveyRepository;

    @Autowired
    public RecipientService(SurveyMapper surveyMapper, RecipientRepository recipientRepository, SurveyRepository surveyRepository) {
        this.surveyMapper = surveyMapper;
        this.recipientRepository = recipientRepository;
        this.surveyRepository = surveyRepository;
    }

    public List<RecipientDTO> getAllRecipients() {
        List<Recipient> recipients = recipientRepository.findAll();
        return recipients.stream().map(surveyMapper::toDto).toList();
    }

    public RecipientDTO getRecipientById(long id) {
        Recipient recipient = recipientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recipient not found with id: " + id));
        return surveyMapper.toDto(recipient);
    }

    @Transactional
    public RecipientDTO createRecipient(RecipientDTO recipientDTO) {
        // Check if email already exists
        if (recipientRepository.existsByEmail(recipientDTO.getEmail())) {
            throw new IllegalStateException("Email already in use: " + recipientDTO.getEmail());
        }

        Recipient recipient = surveyMapper.toEntity(recipientDTO);
        Recipient savedRecipient = recipientRepository.save(recipient);
        return surveyMapper.toDto(savedRecipient);
    }

    @Transactional
    public RecipientDTO updateRecipient(Long id, RecipientDTO recipientDTO) {
        Recipient recipient = recipientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recipient not found with id: " + id));

        // Check email uniqueness if it's changed
        if (!recipient.getEmail().equals(recipientDTO.getEmail()) &&
                recipientRepository.existsByEmail(recipientDTO.getEmail())) {
            throw new IllegalStateException("Email already in use: " + recipientDTO.getEmail());
        }

        recipient.setEmail(recipientDTO.getEmail());
        recipient.setFirstName(recipientDTO.getFirstName());
        recipient.setLastName(recipientDTO.getLastName());
        recipient.setCompany(recipientDTO.getCompany());
        recipient.setType(recipientDTO.getType());

        Recipient updatedRecipient = recipientRepository.save(recipient);
        return surveyMapper.toDto(updatedRecipient);
    }

    public void deleteRecipient(Long id) {
        if (!recipientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recipient not found with id: " + id);
        }
        recipientRepository.deleteById(id);
    }

    @Transactional
    public void addRecipientToSurvey(Long surveyId, Long recipientId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + surveyId));
        Recipient recipient = recipientRepository.findById(recipientId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found with id: " + recipientId));

        survey.getRecipients().add(recipient);
        surveyRepository.save(survey);
    }

    @Transactional
    public void removeRecipientFromSurvey(Long surveyId, Long recipientId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + surveyId));
        Recipient recipient = recipientRepository.findById(recipientId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found with id: " + recipientId));

        survey.getRecipients().remove(recipient);
        surveyRepository.save(survey);
    }

    public List<RecipientDTO> getRecipientsBySurvey(Long surveyId) {
        return recipientRepository.findBySurveysId(surveyId).stream().map(surveyMapper::toDto).toList();
    }
}
