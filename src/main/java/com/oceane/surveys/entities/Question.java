package com.oceane.surveys.entities;

import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "SURVEY")
public class Question {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    public Long questionId;

    @Column(name = "SURVEY_ID")
    public Long surveyId;

    @Column(name = "TEXT")
    public String text;

    @Column(name = "TYPE")
    public String type;

    @Column(name = "REQUIRED")
    public Long required;

    @Column(name = "DISPLAYORDER")
    public Long displayOrder;

    @Column(name = "CONDITIONALLOGIC")
    public String conditionalLogic;

    @OneToMany(mappedBy="questionId")
    private Set<QuestionOption> questionOptions;

    public Question(Long questionId, Long surveyId, String text, String type, Long required, Long displayOrder, String conditionalLogic, Set<QuestionOption> questionOptions) {
        this.questionId = questionId;
        this.surveyId = surveyId;
        this.text = text;
        this.type = type;
        this.required = required;
        this.displayOrder = displayOrder;
        this.conditionalLogic = conditionalLogic;
        this.questionOptions = questionOptions;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getRequired() {
        return required;
    }

    public void setRequired(Long required) {
        this.required = required;
    }

    public Long getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Long displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getConditionalLogic() {
        return conditionalLogic;
    }

    public void setConditionalLogic(String conditionalLogic) {
        this.conditionalLogic = conditionalLogic;
    }

    public Set<QuestionOption> getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(Set<QuestionOption> questionOptions) {
        this.questionOptions = questionOptions;
    }
}
