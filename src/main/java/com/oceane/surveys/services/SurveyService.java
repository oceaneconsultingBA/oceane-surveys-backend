package com.oceane.surveys.services;

import com.oceane.surveys.dto.SurveyCreateDTO;
import com.oceane.surveys.dto.SurveyDTO;
import com.oceane.surveys.entities.Recipient;
import com.oceane.surveys.entities.Survey;
import com.oceane.surveys.entities.SurveyStatus;
import com.oceane.surveys.exception.ResourceNotFoundException;
import com.oceane.surveys.mapper.SurveyMapper;
import com.oceane.surveys.repositories.RecipientRepository;
import com.oceane.surveys.repositories.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SurveyService {
    private final SurveyMapper surveyMapper;
    private final SurveyRepository surveyRepository;
    private final RecipientRepository recipientRepository;

    @Autowired
    public SurveyService(SurveyMapper surveyMapper, SurveyRepository surveyRepository, RecipientRepository recipientRepository) {
        this.surveyMapper = surveyMapper;
        this.surveyRepository = surveyRepository;
        this.recipientRepository = recipientRepository;
    }

    public List<SurveyDTO> getAllSurveys() {
        return surveyRepository.findAll().stream().map(surveyMapper::toDto).toList();
    }

    public SurveyDTO getSurveyById(long id) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + id));;
        return surveyMapper.toDto(survey);
    }

    @Transactional
    public SurveyDTO createSurvey(SurveyCreateDTO createDTO) {
        Survey survey = new Survey();
        survey.setTitle(createDTO.getTitle());
        survey.setDescription(createDTO.getDescription());
        survey.setCreationDate(LocalDateTime.now());
        survey.setLastModifiedDate(LocalDateTime.now());
        survey.setStatus(SurveyStatus.DRAFT);

        // Map and add questions if present
        if (createDTO.getQuestions() != null && !createDTO.getQuestions().isEmpty()) {
            createDTO.getQuestions().forEach(questionDTO -> {
                survey.getQuestions().add(surveyMapper.questionDtoToEntity(questionDTO));
            });
        }

        // Associate recipients if present
        if (createDTO.getRecipientIds() != null && !createDTO.getRecipientIds().isEmpty()) {
            List<Recipient> recipients = recipientRepository.findAllById(createDTO.getRecipientIds());
            recipients.forEach(survey.getRecipients()::add);
        }

        Survey savedSurvey = surveyRepository.save(survey);
        return surveyMapper.toDto(savedSurvey);
    }

    @Transactional
    public SurveyDTO updateSurvey(Long id, SurveyDTO surveyDTO) {
        Survey existingSurvey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + id));

        // Update basic properties
        existingSurvey.setTitle(surveyDTO.getTitle());
        existingSurvey.setDescription(surveyDTO.getDescription());
        existingSurvey.setLastModifiedDate(LocalDateTime.now());

        // Update is only allowed for draft surveys
        if (existingSurvey.getStatus() != SurveyStatus.DRAFT) {
            throw new IllegalStateException("Cannot update a survey that is not in DRAFT status");
        }

        // The updating of questions should be handled by QuestionService
        Survey updatedSurvey = surveyRepository.save(existingSurvey);
        return surveyMapper.toDto(updatedSurvey);
    }

    @Transactional
    public void deleteSurvey(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + id));

        // Only draft surveys can be deleted
        if (survey.getStatus() != SurveyStatus.DRAFT) {
            throw new IllegalStateException("Cannot delete a survey that is not in DRAFT status");
        }

        surveyRepository.deleteById(id);
    }

    public List<SurveyDTO> findByStatus(SurveyStatus status) {
        List<Survey> surveys = surveyRepository.findByStatus(status);
        return surveys.stream().map(surveyMapper::toDto).toList();
    }

    @Transactional
    public SurveyDTO changeSurveyStatus(Long id, SurveyStatus newStatus) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + id));

        // Validate status transitions
        validateStatusTransition(survey.getStatus(), newStatus);

        survey.setStatus(newStatus);
        survey.setLastModifiedDate(LocalDateTime.now());

        // If activating, validate that the survey has questions
        if (newStatus == SurveyStatus.ACTIVE && survey.getQuestions().isEmpty()) {
            throw new IllegalStateException("Cannot activate a survey without questions");
        }

        Survey updatedSurvey = surveyRepository.save(survey);
        return surveyMapper.toDto(updatedSurvey);
    }
    private void validateStatusTransition(SurveyStatus currentStatus, SurveyStatus newStatus) {
        if (currentStatus == newStatus) {
            return; // No change needed
        }

        switch (currentStatus) {
            case DRAFT:
                if (newStatus != SurveyStatus.ACTIVE) {
                    throw new IllegalStateException("Draft survey can only be changed to Active status");
                }
                break;
            case ACTIVE:
                if (newStatus != SurveyStatus.COMPLETED) {
                    throw new IllegalStateException("Active survey can only be changed to Completed status");
                }
                break;
            case COMPLETED:
                throw new IllegalStateException("Completed survey status cannot be changed");
            default:
                throw new IllegalStateException("Unknown survey status: " + currentStatus);
        }
    }

    public List<SurveyDTO> searchByKeyword(String textInTitle) {
        List<Survey> surveys = surveyRepository.findByTitleContainingIgnoreCase(textInTitle);
        return surveys.stream().map(surveyMapper::toDto).toList();
    }
}
