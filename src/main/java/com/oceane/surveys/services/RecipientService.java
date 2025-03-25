package com.oceane.surveys.services;

import com.oceane.surveys.dto.RecipientDTO;
import com.oceane.surveys.dto.SurveyDTO;
import com.oceane.surveys.entities.Recipient;
import com.oceane.surveys.mapper.SurveyMapper;
import com.oceane.surveys.repositories.RecipientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecipientService {
    private final SurveyMapper surveyMapper;
    private final RecipientRepository recipientRepository;
    private final SurveyService surveyService;

    @Autowired
    public RecipientService(SurveyMapper surveyMapper, RecipientRepository recipientRepository, SurveyService surveyService) {
        this.surveyMapper = surveyMapper;
        this.recipientRepository = recipientRepository;
        this.surveyService = surveyService;
    }

    public List<RecipientDTO> getAllRecipients() {
        List<Recipient> recipients = recipientRepository.findAll();
        return recipients.stream().map(surveyMapper::recipientToDto).toList();
    }

    public RecipientDTO getRecipientById(long id) {
        Recipient recipient = recipientRepository.getReferenceById(id);
        return surveyMapper.recipientToDto(recipient);
    }

    @Transactional
    public void createRecipient(RecipientDTO recipientDTO) {
        Recipient recipient = surveyMapper.recipientDtoToEntity(recipientDTO);
        recipient.setRecipientId(null);
        recipientRepository.save(recipient);
    }

    @Transactional
    public void updateRecipient(RecipientDTO recipientDTO) {
        Recipient recipient = surveyMapper.recipientDtoToEntity(recipientDTO);
        recipientRepository.save(recipient);
    }

    @Transactional
    public void deleteRecipient(RecipientDTO recipientDTO) {
        Recipient recipient = surveyMapper.recipientDtoToEntity(recipientDTO);
        recipientRepository.deleteById(recipient.getRecipientId());
    }

    @Transactional
    public void addRecipientToSurvey(SurveyDTO surveyDTO, RecipientDTO recipientDTO) {
        surveyDTO.getRecipients().add(recipientDTO);
        surveyService.updateSurvey(surveyDTO);
    }

    @Transactional
    public void removeRecipientFromSurvey(SurveyDTO surveyDTO, RecipientDTO recipientDTO) {
        surveyDTO.getRecipients().remove(recipientDTO);
        surveyService.updateSurvey(surveyDTO);
    }

    public List<RecipientDTO> getRecipientsBySurvey(SurveyDTO surveyDTO) {
        List<Recipient> recipients = recipientRepository.findBySurveysId(surveyDTO.getId());
        return recipients.stream().map(surveyMapper::recipientToDto).toList();
    }
}
