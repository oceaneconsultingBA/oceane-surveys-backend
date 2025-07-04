package com.oceane.surveys.services;

import com.oceane.surveys.dto.AnswerDTO;
import com.oceane.surveys.dto.StatisticsDTO;
import com.oceane.surveys.dto.SurveyCreateDTO;
import com.oceane.surveys.dto.SurveyDTO;
import com.oceane.surveys.entities.*;
import com.oceane.surveys.exception.ResourceNotFoundException;
import com.oceane.surveys.mapper.SurveyMapper;
import com.oceane.surveys.repositories.AnswerRepository;
import com.oceane.surveys.repositories.QuestionRepository;
import com.oceane.surveys.repositories.RecipientRepository;
import com.oceane.surveys.repositories.SurveyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SurveyService {
    private final SurveyMapper surveyMapper;
    private final SurveyRepository surveyRepository;
    private final RecipientRepository recipientRepository;
    private final AnswerService answerService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public SurveyService(SurveyMapper surveyMapper, SurveyRepository surveyRepository, RecipientRepository recipientRepository,
                         AnswerService answerService,
                         TokenService tokenService, EmailService emailService,
                         AnswerRepository answerRepository,
                         QuestionRepository questionRepository) {
        this.surveyMapper = surveyMapper;
        this.surveyRepository = surveyRepository;
        this.recipientRepository = recipientRepository;
        this.answerService = answerService;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    public StatisticsDTO getStatistics() {
        StatisticsDTO statisticsDTO = new StatisticsDTO();
        statisticsDTO.setActiveSurveys(surveyRepository.countByStatus(SurveyStatus.ACTIVE));
        statisticsDTO.setAnswers(answerRepository.count());
        statisticsDTO.setQuestions(questionRepository.count());
        return statisticsDTO;
    }

    public List<SurveyDTO> getAllSurveys() {
        return surveyRepository.findAll().stream().map(surveyMapper::toDto).toList();
    }

    public SurveyDTO getSurveyById(long id) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + id));
        return surveyMapper.toDto(survey);
    }

    public Map<Long, LocalDateTime> getSurveyAnswerState(long id) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + id));

        Map<Long, LocalDateTime> answerState = new HashMap<>(survey.getRecipients().size());

        for (Recipient recipient : survey.getRecipients()) {
            List<AnswerDTO> answers = answerService.getAnswersBySurveyAndRecipient(survey.getId(), recipient.getId());

            if (answers != null && !answers.isEmpty()) {
                // get(0) because the creation date is the same in all the answers
                answerState.put(recipient.getId(), answers.get(0).getCreationDate());
            } else {
                answerState.put(recipient.getId(), null);
            }
        }

        return answerState;
    }

    @Transactional
    public SurveyDTO createSurvey(SurveyCreateDTO createDTO) {
        Survey survey = new Survey();
        survey.setTitle(createDTO.getTitle());
        survey.setDescription(createDTO.getDescription());
        survey.setCreationDate(LocalDateTime.now());
        survey.setLastModifiedDate(LocalDateTime.now());
        survey.setStatus(createDTO.getStatus() != null ? createDTO.getStatus() : SurveyStatus.DRAFT);

        // Map and add questions if present
        if (createDTO.getQuestions() != null && !createDTO.getQuestions().isEmpty()) {
            List<Question> questions = createDTO.getQuestions().stream()
                    .map(questionDTO -> {
                        Question question = surveyMapper.toEntity(questionDTO);
                        question.setSurvey(survey);
                        return question;
                    })
                    .toList();
            survey.getQuestions().addAll(questions);
        }

        // Associate recipients if present
        if (createDTO.getRecipientIds() != null && !createDTO.getRecipientIds().isEmpty()) {
            List<Recipient> recipients = recipientRepository.findAllById(createDTO.getRecipientIds());
            recipients.forEach(survey.getRecipients()::add);
        }

        Survey savedSurvey = surveyRepository.save(survey);

        // Si l'enquête est passée à ACTIVE, envoyer les emails
        log.info("changingToActive = {}", savedSurvey.getStatus());
        if (savedSurvey.getStatus() == SurveyStatus.ACTIVE) {
            sendSurveyEmails(savedSurvey);
        }

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

        // Si l'enquête était en DRAFT et qu'on demande de la passer en ACTIVE
        boolean wasInDraft = existingSurvey.getStatus() == SurveyStatus.DRAFT;
        boolean changingToActive = surveyDTO.getStatus() == SurveyStatus.ACTIVE;

        // Vérifier si on peut modifier l'enquête
        if (!wasInDraft && !existingSurvey.getStatus().equals(surveyDTO.getStatus())) {
            throw new IllegalStateException("Cannot update status of a survey that is not in DRAFT status");
        }

        // Mise à jour du statut
        existingSurvey.setStatus(surveyDTO.getStatus());

        // Supprimer toutes les questions et leurs options (via orphanRemoval=true)
        existingSurvey.getQuestions().clear();

        // Ajouter les nouvelles questions et options
        if (surveyDTO.getQuestions() != null) {
            log.info("{} question(s)", surveyDTO.getQuestions().size());
            surveyDTO.getQuestions().forEach(questionDTO -> {
                Question question = surveyMapper.toEntity(questionDTO);
                question.setSurvey(existingSurvey);
                existingSurvey.getQuestions().add(question);
            });
        }

        // Gérer les destinataires
        existingSurvey.getRecipients().clear();
        if (surveyDTO.getRecipientIds() != null && !surveyDTO.getRecipientIds().isEmpty()) {
            log.info("{} destinataire(s)", surveyDTO.getRecipientIds().size());
            List<Recipient> recipients = recipientRepository.findAllById(surveyDTO.getRecipientIds());
            recipients.forEach(existingSurvey.getRecipients()::add);
        }

        Survey updatedSurvey = surveyRepository.save(existingSurvey);

        // Si l'enquête est passée de DRAFT à ACTIVE, envoyer les emails
        log.info("wasInDraft = {}; changingToActive = {}", wasInDraft, changingToActive);
        if (wasInDraft && changingToActive) {
            sendSurveyEmails(updatedSurvey);
        }

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

    /**
     * Méthode privée pour envoyer les emails aux destinataires d'une enquête
     */
    private void sendSurveyEmails(Survey survey) {
        // Vérification que l'enquête a au moins une question
        if (survey.getQuestions() == null || survey.getQuestions().isEmpty()) {
            throw new IllegalStateException("L'enquête doit contenir au moins une question");
        }

        // Vérification que l'enquête a au moins un destinataire
        if (survey.getRecipients() == null || survey.getRecipients().isEmpty()) {
            throw new IllegalStateException("L'enquête doit avoir au moins un destinataire");
        }

        log.info("Envoi un mail aux {} destinataire(s)", survey.getRecipients().size());

        // Génération et envoi d'emails pour chaque destinataire
        for (Recipient recipient : survey.getRecipients()) {
            try {
                // Générer un token unique pour ce destinataire et cette enquête
                var token = tokenService.generateTokenForRecipient(survey, recipient);
                log.info("Email à envoyer avec le token {}", token);

                // Construire l'URL de l'enquête avec le token
                String surveyUrl = tokenService.buildSurveyUrl(token);

                // Envoyer l'email d'invitation
                emailService.sendSurveyInvitation(survey, recipient, surveyUrl);

                log.info("Email envoyé à {} pour l'enquête {}", recipient.getEmail(), survey.getTitle());
            } catch (Exception e) {
                // Log l'erreur mais continue le traitement des autres destinataires
                log.error("Erreur lors de l'envoi à {}: {}", recipient.getEmail(), e.getMessage());
            }
        }

        log.info("Enquête {} validée et emails envoyés avec succès", survey.getTitle());
    }
}
