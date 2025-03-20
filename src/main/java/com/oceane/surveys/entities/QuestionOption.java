package com.oceane.surveys.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "SURVEY")
public class QuestionOption {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    public Long questionoptionId;

    @Column(name = "QUESTION_ID")
    public Long questionId;

    @Column(name = "TEXT")
    public String text;

    @Column(name = "DISPLAYORDER")
    public Long displayOrder;

    public QuestionOption(Long questionoptionId, Long questionId, String text, Long displayOrder) {
        this.questionoptionId = questionoptionId;
        this.questionId = questionId;
        this.text = text;
        this.displayOrder = displayOrder;
    }

    public Long getQuestionoptionId() {
        return questionoptionId;
    }

    public void setQuestionoptionId(Long questionoptionId) {
        this.questionoptionId = questionoptionId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Long displayOrder) {
        this.displayOrder = displayOrder;
    }
}
