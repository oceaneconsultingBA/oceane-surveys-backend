package com.oceane.surveys.services;

import com.oceane.surveys.dto.RecipientDTO;
import com.oceane.surveys.dto.SurveyDTO;
import com.oceane.surveys.dto.TokenDTO;
import com.oceane.surveys.entities.Recipient;
import com.oceane.surveys.entities.Survey;
import com.oceane.surveys.entities.SurveyToken;
import com.oceane.surveys.mapper.SurveyMapper;
import com.oceane.surveys.repositories.SurveyTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenService {
    @Value("${aws.survey.baseUrl}")
    private String surveyBaseUrl;

    private final SurveyMapper surveyMapper;

    private final SurveyTokenRepository tokenRepository;

    @Autowired
    public TokenService(
            SurveyMapper surveyMapper,
            SurveyTokenRepository tokenRepository
    ) {
        this.surveyMapper = surveyMapper;
        this.tokenRepository = tokenRepository;
    }

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
        TokenDTO token = getToken(tokenValue);
        return token != null;
    }

    /**
     * Retourne un token purge.
     *
     * @param tokenValue
     * @return
     */
    public TokenDTO getToken(String tokenValue) {
        var tokenOpt = tokenRepository.findByToken(tokenValue);

        if (tokenOpt.isPresent()) {
            SurveyToken token = tokenOpt.get();
            if (!token.isUsed() && token.getExpiresAt().isAfter(LocalDateTime.now())) {
                return surveyMapper.toDto(token);
            }
        }

        return null;
    }

    /**
     * Vérifie si un token est valide (existe, non utilisé, non expiré)
     */
    public SurveyDTO getSurvey(String tokenValue) {
        TokenDTO token = getToken(tokenValue);

        if (token != null) {
            return token.getSurvey();
        }

        return null;
    }

    /**
     * Retourne le destinataire.
     */
    public RecipientDTO getRecipient(String tokenValue) {
        TokenDTO token = getToken(tokenValue);

        if (token != null) {
            return token.getRecipient();
        }

        return null;
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
