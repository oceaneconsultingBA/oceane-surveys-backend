package com.oceane.surveys.services;

import com.oceane.surveys.dto.QuestionDTO;
import com.oceane.surveys.dto.SurveyDTO;
import com.oceane.surveys.entities.Question;
import com.oceane.surveys.mapper.SurveyMapper;
import com.oceane.surveys.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {
    private final SurveyMapper surveyMapper;
    private final QuestionRepository questionRepository;
    private final SurveyService surveyService;

    @Autowired
    public QuestionService(SurveyMapper surveyMapper, QuestionRepository questionRepository, SurveyService surveyService) {
        this.surveyMapper = surveyMapper;
        this.questionRepository = questionRepository;
        this.surveyService = surveyService;
    }

    public void addQuestionToSurvey(SurveyDTO surveyDTO, QuestionDTO questionDTO) {
        surveyDTO.getQuestions().add(questionDTO);
        surveyService.updateSurvey(surveyDTO);
    }

    @Transactional
    public void updateQuestion(QuestionDTO questionDTO) {
        Question question = surveyMapper.questionDtoToEntity(questionDTO);
        questionRepository.save(question);
    }

    @Transactional
    public void deleteQuestion(QuestionDTO questionDTO) {
        Question question = surveyMapper.questionDtoToEntity(questionDTO);
        questionRepository.deleteById(question.getQuestionId());
    }
}
