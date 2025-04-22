package com.oceane.surveys.dto;

import java.time.LocalDateTime;

import com.oceane.surveys.entities.Recipient;
import com.oceane.surveys.entities.Survey;

public class SurveyTokenDTO {

    private String token;
    private LocalDateTime expirationDate;
    private String status;
    private Survey survey;
    private Recipient recipient;
    private String errorMsg;

    public SurveyTokenDTO() {}

    public SurveyTokenDTO(String errorMsg) {
        this.errorMsg = errorMsg;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }
}
