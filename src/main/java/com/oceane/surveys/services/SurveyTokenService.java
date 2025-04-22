package com.oceane.surveys.services;

import com.oceane.surveys.dto.RecipientDTO;
import com.oceane.surveys.dto.SurveyDTO;
import com.oceane.surveys.dto.SurveyTokenDTO;
import com.oceane.surveys.entities.*;
import com.oceane.surveys.mapper.SurveyMapper;
import com.oceane.surveys.repositories.SurveyTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class SurveyTokenService {

    private final SurveyTokenRepository tokenRepository;
    private final NotificationService notificationService;
    private final SurveyMapper surveyMapper;

    public SurveyTokenService(SurveyTokenRepository tokenRepository,
                              NotificationService notificationService, 
                              SurveyMapper surveyMapper) {
        this.tokenRepository = tokenRepository;
        this.notificationService = notificationService;
        this.surveyMapper = surveyMapper;
    }
    public SurveyToken generateToken(Survey survey,  Recipient recipient) {
        SurveyToken token = new SurveyToken();
        token.setSurvey(survey);
        token.setRecipient(recipient);
        token.setToken(UUID.randomUUID().toString());
        token.setStatus(TokenStatus.ACTIVE);
        token.setExpirationDate(LocalDateTime.now().plusDays(30));

        return tokenRepository.save(token);
    }

    public void generateTokensAndSendEmails(Survey survey) {
        if (survey.getStatus() != SurveyStatus.ACTIVE || 
            survey.getRecipients() == null || 
            survey.getRecipients().isEmpty()) {
        return; 
        }
    
        for (Recipient recipient : survey.getRecipients()) {
            SurveyToken token = generateToken(survey, recipient);
            if (token != null) {
               notificationService.sendSurveyToken(token, recipient);
            }
        }
    }

    public Optional<SurveyTokenDTO> getSurveyTokenDTOByValidToken(String token) {
        return tokenRepository.findByToken(token)
                .filter(t -> t.getStatus() == TokenStatus.ACTIVE)
                .flatMap(t -> {
                    if (t.getExpirationDate().isBefore(LocalDateTime.now())) {
                        t.setStatus(TokenStatus.EXPIRED);
                        tokenRepository.save(t);
                        return Optional.empty();
                    }
                    return Optional.of(surveyMapper.toDTO(t));
                });
    }
    
    

    public boolean markTokenAsUsed(String tokenValue) {
        Optional<SurveyToken> tokenOpt = tokenRepository.findByToken(tokenValue)
                .filter(token -> token.getStatus() == TokenStatus.ACTIVE);
        tokenOpt.ifPresent(token -> {
            token.setStatus(TokenStatus.USED);
            tokenRepository.save(token);
        });
        return tokenOpt.isPresent();
    }
    
    
    
}
