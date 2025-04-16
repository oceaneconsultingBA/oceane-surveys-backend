package com.oceane.surveys.services;

import org.springframework.stereotype.Service;

import com.oceane.surveys.component.AwsSesEmailSender;
import com.oceane.surveys.entities.Recipient;
import com.oceane.surveys.entities.SurveyToken;


@Service
public class EmailNotificationService implements NotificationService {

    private final AwsSesEmailSender awsSesEmailSender;

    public EmailNotificationService(AwsSesEmailSender awsSesEmailSender) {
        this.awsSesEmailSender = awsSesEmailSender;
    }

    @Override
    public void sendSurveyToken(SurveyToken token, Recipient recipient) {
        awsSesEmailSender.sendSurveyTokenEmail(token, recipient);
    }
}
