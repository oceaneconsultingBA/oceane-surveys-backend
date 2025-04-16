package com.oceane.surveys.services;
import com.oceane.surveys.entities.Recipient;
import com.oceane.surveys.entities.SurveyToken;

public interface NotificationService {
    void sendSurveyToken(SurveyToken token, Recipient recipient);
}
