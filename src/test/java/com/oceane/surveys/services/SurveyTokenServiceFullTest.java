package com.oceane.surveys.services;

import com.oceane.surveys.entities.*;
import com.oceane.surveys.repositories.RecipientRepository;
import com.oceane.surveys.repositories.SurveyRepository;
import com.oceane.surveys.repositories.SurveyTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SurveyTokenServiceFullTest {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private RecipientRepository recipientRepository;

    @Autowired
    private SurveyTokenRepository tokenRepository;

    @Autowired
    private SurveyTokenService surveyTokenService;

    private Survey survey;
    private List<Recipient> recipients;

    @BeforeEach
    void setUp() {
        // Créer un survey actif
        survey = new Survey();
        survey.setTitle("Customer Satisfaction");
        survey.setDescription("Survey for product feedback");
        survey.setCreationDate(LocalDateTime.now());
        survey.setLastModifiedDate(LocalDateTime.now());
        survey.setStatus(SurveyStatus.ACTIVE);
        survey = surveyRepository.save(survey);

        // Créer plusieurs destinataires
        recipients = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Recipient r = new Recipient();
            r.setFirstName("Recipient" + i);
            r.setLastName("Test");
            r.setEmail("recipient" + i + "@example.com");
            r.setType(RecipientType.FREELANCE);
            recipients.add(r);
        }
        recipients = recipientRepository.saveAll(recipients);

        // Associer au survey
        survey.getRecipients().addAll(recipients);
        survey = surveyRepository.save(survey);
    }

    @Test
    void generateToken_shouldCreateUniqueTokensForEachRecipient() {
        for(Recipient r : recipients){
            surveyTokenService.generateToken(survey, r);
        }

        List<SurveyToken> tokens = tokenRepository.findAll();

        // Vérifie qu’un token est généré pour chaque destinataire
        assertEquals(recipients.size(), tokens.size(), "Il doit y avoir un token par destinataire");

        // Vérifie que chaque token est unique
        Set<String> uniqueTokens = tokens.stream()
                .map(SurveyToken::getToken)
                .collect(Collectors.toSet());

        assertEquals(tokens.size(), uniqueTokens.size(), "Chaque token doit être unique");

        // Vérifie les propriétés de chaque token
        for (SurveyToken token : tokens) {
            assertNotNull(token.getToken(), "Le token ne doit pas être nul");
            assertEquals(survey.getId(), token.getSurvey().getId(), "Le token doit être associé au bon survey");
            assertTrue(recipients.stream().anyMatch(r -> r.getId().equals(token.getRecipient().getId())),
                    "Le token doit être associé à un destinataire valide");
            assertEquals(TokenStatus.ACTIVE, token.getStatus(), "Le statut du token doit être ACTIVE");
            assertTrue(token.getExpirationDate().isAfter(LocalDateTime.now()), "Le token doit avoir une date d’expiration future");
        }
    }
}

