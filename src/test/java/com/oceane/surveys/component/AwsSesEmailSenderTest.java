package com.oceane.surveys.component;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Value;

import com.oceane.surveys.entities.Recipient;
import com.oceane.surveys.entities.SurveyToken;

import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

public class AwsSesEmailSenderTest {
private SesClient sesClient;
    private AwsSesEmailSender emailSender;
    @Value("${app.email.source}")
    private String sourceEmail;

    @Value("${app.survey.base-url}")
    private String baseUrl;

    @BeforeEach
    void setUp() {
        sesClient = mock(SesClient.class);
        emailSender = new AwsSesEmailSender(sesClient);
    }

    @Test
    void shouldSendSurveyTokenEmail() {
        // Arrange
        SurveyToken token = new SurveyToken();
        token.setToken("123456");

        Recipient recipient = new Recipient();
        recipient.setFirstName("Karim");
        recipient.setEmail("ko@oceane.com");

        // Act
        emailSender.sendSurveyTokenEmail(token, recipient);

        // Assert
        ArgumentCaptor<SendEmailRequest> captor = ArgumentCaptor.forClass(SendEmailRequest.class);
        verify(sesClient, times(1)).sendEmail(captor.capture());

        SendEmailRequest requestSent = captor.getValue();

        assertTrue(requestSent.destination().toAddresses().contains("ko@oceane.com"));
        assertTrue(requestSent.message().subject().data().contains("Votre lien d'accès à l'enquête Oceane consulting"));
        assertTrue(requestSent.message().body().text().data().contains(baseUrl+"/123456"));
    }
}
