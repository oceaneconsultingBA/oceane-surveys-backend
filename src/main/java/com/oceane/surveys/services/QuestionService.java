package com.oceane.surveys.services;

import com.oceane.surveys.dto.QuestionDTO;
import com.oceane.surveys.dto.QuestionOptionDTO;
import com.oceane.surveys.entities.Question;
import com.oceane.surveys.entities.QuestionOption;
import com.oceane.surveys.entities.Survey;
import com.oceane.surveys.entities.SurveyStatus;
import com.oceane.surveys.exception.ResourceNotFoundException;
import com.oceane.surveys.mapper.SurveyMapper;
import com.oceane.surveys.repositories.QuestionOptionRepository;
import com.oceane.surveys.repositories.QuestionRepository;
import com.oceane.surveys.repositories.SurveyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class QuestionService {
    private final SurveyMapper surveyMapper;
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionOptionRepository optionRepository;


    @Autowired
    public QuestionService(SurveyMapper surveyMapper, QuestionRepository questionRepository, SurveyRepository surveyRepository, QuestionOptionRepository optionRepository) {
        this.surveyMapper = surveyMapper;
        this.questionRepository = questionRepository;
        this.surveyRepository = surveyRepository;
        this.optionRepository = optionRepository;
    }

    @Transactional
    public QuestionDTO addQuestionToSurvey(Long surveyId, QuestionDTO questionDTO) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + surveyId));

        // Verify survey is in DRAFT status
        if (survey.getStatus() != SurveyStatus.DRAFT) {
            throw new IllegalStateException("Cannot add questions to a survey that is not in DRAFT status");
        }

        Question question = surveyMapper.questionDtoToEntity(questionDTO);
        question.setSurvey(survey);

        // Set display order if not provided
        if (question.getDisplayOrder() == 0) {
            question.setDisplayOrder(survey.getQuestions().size() + 1);
        }

        Question savedQuestion = questionRepository.save(question);

        // Update survey last modified date
        survey.setLastModifiedDate(LocalDateTime.now());
        surveyRepository.save(survey);

        return surveyMapper.questionToDto(savedQuestion);
    }

    @Transactional
    public QuestionDTO updateQuestion(Long questionId, QuestionDTO questionDTO) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + questionId));

        // Check if associated survey is in draft status
        if (question.getSurvey().getStatus() != SurveyStatus.DRAFT) {
            throw new IllegalStateException("Cannot update questions of a survey that is not in DRAFT status");
        }

        // Update question properties
        question.setText(questionDTO.getText());
        question.setType(questionDTO.getType());
        question.setRequired(questionDTO.isRequired());
        question.setDisplayOrder(questionDTO.getDisplayOrder());
        question.setConditionalLogic(questionDTO.getConditionalLogic());

        // Handle options update
        if (questionDTO.getOptions() != null) {
            // Remove existing options and add new ones
            optionRepository.deleteByQuestionId(questionId);
            question.getOptions().clear();
            log.info("{} option(s) for the question {}", questionDTO.getOptions().size(), questionDTO.getText());

            for (QuestionOptionDTO optionDTO : questionDTO.getOptions()) {
                QuestionOption option = new QuestionOption();
                option.setText(optionDTO.getText());
                option.setDisplayOrder(optionDTO.getDisplayOrder());
                option.setQuestion(question);
                question.getOptions().add(option);
            }
        }

        // Update survey last modified date
        Survey survey = question.getSurvey();
        survey.setLastModifiedDate(LocalDateTime.now());
        surveyRepository.save(survey);

        Question updatedQuestion = questionRepository.save(question);
        return surveyMapper.questionToDto(updatedQuestion);
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + questionId));

        // Check if associated survey is in draft status
        if (question.getSurvey().getStatus() != SurveyStatus.DRAFT) {
            throw new IllegalStateException("Cannot delete questions of a survey that is not in DRAFT status");
        }

        // Update survey last modified date
        Survey survey = question.getSurvey();
        survey.setLastModifiedDate(LocalDateTime.now());
        surveyRepository.save(survey);

        // Remove the question
        questionRepository.deleteById(questionId);
    }

    public List<QuestionDTO> getQuestionsBySurvey(Long surveyId) {
        return questionRepository.findBySurvey_IdOrderByDisplayOrderAsc(surveyId).stream()
                .map(surveyMapper::questionToDto)
                .toList();
    }
}
