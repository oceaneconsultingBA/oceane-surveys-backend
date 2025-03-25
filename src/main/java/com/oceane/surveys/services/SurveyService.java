package com.oceane.surveys.services;

import com.oceane.surveys.dto.SurveyDTO;
import com.oceane.surveys.entities.Survey;
import com.oceane.surveys.entities.SurveyStatus;
import com.oceane.surveys.mapper.SurveyMapper;
import com.oceane.surveys.repositories.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SurveyService {
    private final SurveyMapper surveyMapper;
    private final SurveyRepository surveyRepository;

    @Autowired
    public SurveyService(SurveyMapper surveyMapper, SurveyRepository surveyRepository) {
        this.surveyMapper = surveyMapper;
        this.surveyRepository = surveyRepository;
    }

    public List<SurveyDTO> getAllSurveys() {
        List<Survey> surveys = surveyRepository.findAll();
        return surveys.stream().map(surveyMapper::toDto).toList();
    }

    public SurveyDTO getSurveyById(long id) {
        Survey survey = surveyRepository.getReferenceById(id);
        return surveyMapper.toDto(survey);
    }

    @Transactional
    public void createSurvey(SurveyDTO surveyDTO) {
        Survey survey = surveyMapper.toEntity(surveyDTO);
        survey.setSurveyId(null);
        surveyRepository.save(survey);
    }

    @Transactional
    public void updateSurvey(SurveyDTO surveyDTO) {
        Survey survey = surveyMapper.toEntity(surveyDTO);
        surveyRepository.save(survey);
    }

    @Transactional
    public void deleteSurvey(SurveyDTO surveyDTO) {
        Survey survey = surveyMapper.toEntity(surveyDTO);
        surveyRepository.deleteById(survey.getSurveyId());
    }

    public List<SurveyDTO> findByStatus(SurveyStatus status) {
        List<Survey> surveys = surveyRepository.findByStatus(status);
        return surveys.stream().map(surveyMapper::toDto).toList();
    }

    @Transactional
    public void changeSurveyStatus(SurveyDTO survey, SurveyStatus status) {
        survey.setStatus(status);
        updateSurvey(survey);
    }

    public List<SurveyDTO> searchByKeyword(String textInTitle) {
        List<Survey> surveys = surveyRepository.findByTitleContainingIgnoreCase(textInTitle);
        return surveys.stream().map(surveyMapper::toDto).toList();
    }
}
