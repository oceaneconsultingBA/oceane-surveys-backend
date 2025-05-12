package com.oceane.surveys.services;

import com.oceane.surveys.entities.Recipient;
import com.oceane.surveys.entities.Survey;
import com.oceane.surveys.entities.SurveyToken;
import com.oceane.surveys.repositories.SurveyTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenService {

    @Autowired
    private SurveyTokenRepository tokenRepository;

    @Value("${aws.survey.baseUrl}")
    private String surveyBaseUrl;

    /**
     * Génère un token unique pour un destinataire d'enquête
     */
    public SurveyToken generateTokenForRecipient(Survey survey, Recipient recipient) {
        // Vérifier si un token existe déjà pour ce destinataire et cette enquête
        var existingTokens = tokenRepository.findBySurveyIdAndRecipientId(survey.getId(), recipient.getId());
        if (!existingTokens.isEmpty()) {
            return existingTokens.get(0); // Retourner le token existant
        }

        // Créer un nouveau token
        String tokenValue;
        do {
            tokenValue = UUID.randomUUID().toString();
        } while (tokenRepository.existsByToken(tokenValue));

        SurveyToken token = new SurveyToken();
        token.setToken(tokenValue);
        token.setSurvey(survey);
        token.setRecipient(recipient);
        token.setUsed(false);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusDays(30));

        return tokenRepository.save(token);
    }

    /**
     * Construit l'URL complète pour accéder à l'enquête avec le token
     */
    public String buildSurveyUrl(SurveyToken token) {
        return surveyBaseUrl + token.getToken();
    }

    /**
     * Vérifie si un token est valide (existe, non utilisé, non expiré)
     */
    public boolean isTokenValid(String tokenValue) {
        var tokenOpt = tokenRepository.findByToken(tokenValue);
        if (tokenOpt.isEmpty()) {
            return false;
        }

        SurveyToken token = tokenOpt.get();
        return !token.isUsed() && token.getExpiresAt().isAfter(LocalDateTime.now());
    }

    /**
     * Marque un token comme utilisé
     */
    public void markTokenAsUsed(String tokenValue) {
        var tokenOpt = tokenRepository.findByToken(tokenValue);
        if (tokenOpt.isPresent()) {
            SurveyToken token = tokenOpt.get();
            token.setUsed(true);
            tokenRepository.save(token);
        }
    }
}
