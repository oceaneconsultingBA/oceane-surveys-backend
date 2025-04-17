package com.oceane.surveys.services;

import com.oceane.surveys.dto.AnswerDTO;
import com.oceane.surveys.dto.QuestionDTO;
import com.oceane.surveys.dto.QuestionOptionDTO;
import com.oceane.surveys.entities.*;
import com.oceane.surveys.exception.ResourceNotFoundException;
import com.oceane.surveys.mapper.SurveyMapper;
import com.oceane.surveys.repositories.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnswerService {
    private final SurveyMapper surveyMapper;
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;
    private final AnswerRepository answerRepository;
    private final RecipientRepository recipientRepository;

    @Autowired
    public AnswerService(
            SurveyMapper surveyMapper,
            QuestionRepository questionRepository,
            SurveyRepository surveyRepository,
            AnswerRepository answerRepository,
            RecipientRepository recipientRepository
    ) {
        this.surveyMapper = surveyMapper;
        this.questionRepository = questionRepository;
        this.surveyRepository = surveyRepository;
        this.answerRepository = answerRepository;
        this.recipientRepository = recipientRepository;
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
    public Map<Long, AnswerDTO> saveAnswers(Long surveyId, @Valid Map<Long, AnswerDTO> answerDTOsByQuestionId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + surveyId));

        // Verify survey is in ACTIVE status
        if (survey.getStatus() != SurveyStatus.ACTIVE) {
            throw new IllegalStateException("Cannot add answers to a survey that is not in ACTIVE status");
        }

        Map<Long, AnswerDTO> savedAnswers = new HashMap<>(answerDTOsByQuestionId.size());

        for (Map.Entry<Long, AnswerDTO> questionIdAndAnswer : answerDTOsByQuestionId.entrySet()) {
            Long questionId = questionIdAndAnswer.getKey();
            AnswerDTO answerDTO = questionIdAndAnswer.getValue();
            Recipient recipient = recipientRepository.findById(answerDTO.getRecipientId()).orElseThrow(() -> new ResourceNotFoundException("Recipient not found with id: " + answerDTO.getRecipientId()));
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

        return savedAnswers;
    }
}
