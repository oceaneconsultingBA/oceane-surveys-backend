package com.oceane.surveys.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.oceane.surveys.entities.Recipient;
import com.oceane.surveys.entities.SurveyToken;

import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Component
public class AwsSesEmailSender {

        private final SesClient sesClient;

        @Value("${app.email.source}")
        private String sourceEmail;
    
        @Value("${app.survey.base-url}")
        private String baseSurveyUrl;
    
        public AwsSesEmailSender(SesClient sesClient) {
            this.sesClient = sesClient;
        }
    
        public void sendSurveyTokenEmail(SurveyToken token, Recipient recipient) {
            String subject = buildSubject();
            String body = buildBody(token, recipient);
    
            SendEmailRequest request = SendEmailRequest.builder()
                    .destination(Destination.builder().toAddresses(recipient.getEmail()).build())
                    .message(Message.builder()
                            .subject(Content.builder().data(subject).build())
                            .body(Body.builder()
                                    .text(Content.builder().data(body).build())
                                    .build())
                            .build())
                    .source(sourceEmail)
                    .build();
    
            sesClient.sendEmail(request);
        }
    
        private String buildSubject() {
            return "Votre lien d'accès à l'enquête Oceane consulting";
        }
    
        private String buildBody(SurveyToken token, Recipient recipient) {
            return String.format(
                    "Bonjour %s,\n\nVeuillez accéder à l'enquête via ce lien : %s",
                    recipient.getFirstName(),
                    baseSurveyUrl + "/" + token.getToken()
            );
        }
}
