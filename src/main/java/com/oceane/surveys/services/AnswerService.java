package com.oceane.surveys.services;

import com.oceane.surveys.dto.AnswerDTO;
import com.oceane.surveys.dto.RecipientDTO;
import com.oceane.surveys.dto.SurveyDTO;
import com.oceane.surveys.entities.*;
import com.oceane.surveys.exception.ResourceNotFoundException;
import com.oceane.surveys.mapper.SurveyMapper;
import com.oceane.surveys.repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class AnswerService {
    private final SurveyMapper surveyMapper;
    private final EntityManager entityManager;
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;
    private final AnswerRepository answerRepository;
    private final RecipientRepository recipientRepository;
    private final TokenService tokenService;

    @Autowired
    public AnswerService(
            SurveyMapper surveyMapper,
            EntityManager entityManager,
            QuestionRepository questionRepository,
            SurveyRepository surveyRepository,
            AnswerRepository answerRepository,
            RecipientRepository recipientRepository,
            TokenService tokenService
    ) {
        this.surveyMapper = surveyMapper;
        this.questionRepository = questionRepository;
        this.entityManager = entityManager;
        this.surveyRepository = surveyRepository;
        this.answerRepository = answerRepository;
        this.recipientRepository = recipientRepository;
        this.tokenService = tokenService;
    }

    public List<AnswerDTO> getAnswersBySurvey(Long surveyId) {
        return answerRepository.findBySurvey_IdOrderByDisplayOrderAsc(surveyId).stream()
                .map(surveyMapper::answerToDto)
                .toList();
    }

    public List<AnswerDTO> getAnswersBySurveyAndRecipient(Long surveyId, Long recipientId) {
        return answerRepository.findBySurvey_IdAndRecipient_IdOrderByDisplayOrderAsc(surveyId, recipientId).stream()
                .map(surveyMapper::answerToDto)
                .toList();
    }

    @Transactional
    public Map<Long, AnswerDTO> saveAnswers(Long surveyId, String tokenValue, @Valid Map<Long, AnswerDTO> answerDTOsByQuestionId) {
        log.info("{} answer(s) to save", answerDTOsByQuestionId.size());
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + surveyId));

        SurveyDTO surveyFromToken = tokenService.getSurvey(tokenValue);

        if (surveyFromToken == null || !Objects.equals(surveyFromToken.getId(), survey.getId())) {
            throw new IllegalStateException("No valid token associated to the right survey");
        }

        RecipientDTO recipientFromToken = tokenService.getRecipient(tokenValue);

        // Verify survey is in ACTIVE status
        if (survey.getStatus() != SurveyStatus.ACTIVE) {
            throw new IllegalStateException("Cannot add answers to a survey that is not in ACTIVE status");
        }

        Map<Long, AnswerDTO> savedAnswers = new HashMap<>(answerDTOsByQuestionId.size());

        for (Map.Entry<Long, AnswerDTO> questionIdAndAnswer : answerDTOsByQuestionId.entrySet()) {
            Long questionId = questionIdAndAnswer.getKey();
            AnswerDTO answerDTO = questionIdAndAnswer.getValue();
            Recipient recipient = recipientRepository.findById(answerDTO.getRecipientId()).orElseThrow(() -> new ResourceNotFoundException("Recipient not found with id: " + answerDTO.getRecipientId()));

            if (!Objects.equals(recipientFromToken.getId(), recipient.getId())) {
                throw new IllegalStateException("No valid token associated to the right recipient");
            }

            Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + questionId));
            Answer answer = surveyMapper.answerDtoToEntity(answerDTO);
            answer.setId(null);
            answer.setQuestion(question);
            answer.setRecipient(recipient);

            if (answer.getOptions() != null) {
                for (QuestionOption questionOption : answer.getOptions()) {
                    questionOption.setQuestion(question);
                }
            }

            Answer savedAnswer = answerRepository.save(answer);
            AnswerDTO savedAnswerDTO = surveyMapper.answerToDto(savedAnswer);
            savedAnswers.put(questionId, savedAnswerDTO);
        }

        tokenService.markTokenAsUsed(tokenValue);

        return savedAnswers;
    }

    public Map<String, Long> getAnswersByDate(String periodicity) {
        String format = switch (periodicity) {
            case "year" -> "yyyy";
            case "month" -> "yyyy-MM";
            default -> "yyyy-MM-dd";
        };
        //Query query = entityManager.createQuery("select answer.creationDate, count(answer.id) as answerCount from Answer answer order by answer.creationDate desc group by answer.creationDate");
        Query query = entityManager.createQuery("select to_char(answer.creationDate, '" + format + "'), count(answer.id) as answerCount from Answer answer group by to_char(answer.creationDate, '" + format + "') order by to_char(answer.creationDate, '" + format + "')");
        List results = query.getResultList();
        log.info("{} period(s) for answers", results.size());

        Map<String, Long> output = new HashMap<>(results.size());

        for (Object result : results) {
            Object[] row = (Object[]) result;

            if (row[0] != null) {
                output.put((String) row[0], (Long) row[1]);
            }
        }
        return output;
    }

    public Map<String, Long> getAnswersByDelay(String periodicity) {
        String format = switch (periodicity) {
            case "year" -> "year";
            case "month" -> "month";
            default -> "day";
        };
        //Query query = entityManager.createQuery("select answer.creationDate, count(answer.id) as answerCount from Answer answer order by answer.creationDate desc group by answer.creationDate");
        Query query = entityManager.createQuery("select DATEDIFF(" + format + ", answer.question.survey.creationDate, answer.creationDate), count(answer.id) as answerCount from Answer answer group by DATEDIFF(" + format + ", answer.question.survey.creationDate, answer.creationDate) order by DATEDIFF(" + format + ", answer.question.survey.creationDate, answer.creationDate)");
        List results = query.getResultList();
        log.info("{} period(s) for delayed answers", results.size());

        Map<String, Long> output = new HashMap<>(results.size());

        for (Object result : results) {
            Object[] row = (Object[]) result;

            if (row[0] != null) {
                output.put(String.valueOf((Long) row[0]), (Long) row[1]);
            }
        }
        return output;
    }
}
