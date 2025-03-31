package com.oceane.surveys.entities;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions")
public class Question {
    /**
     *
     */
    @Id
    @Column(name = "question_id")
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    /**
     * Enquête parente
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    /**
     * Libellé de la question
     */
    @Column(name = "text", nullable = false)
    private String text;

    /**
     * Type de question
     */
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType type;

    /**
     * Indique si la réponse est obligatoire
     */
    @Column(name = "required", nullable = false)
    private boolean required;

    /**
     * Ordre d'affichage dans l'enquête
     */
    @Column(name = "display_order")
    private Integer displayOrder;

    /**
     * Logique conditionnelle d'affichage (format JSON, optionnel)
     */
    @Column(name = "conditional_logic", columnDefinition = "TEXT")
    private String conditionalLogic;

    /**
     * Options de réponse (pour questions à choix)
     */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOption> options = new ArrayList<>();

    // Helper methods
    public void addOption(QuestionOption option) {
        options.add(option);
        option.setQuestion(this);
    }

    public void removeOption(QuestionOption option) {
        options.remove(option);
        option.setQuestion(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getConditionalLogic() {
        return conditionalLogic;
    }

    public void setConditionalLogic(String conditionalLogic) {
        this.conditionalLogic = conditionalLogic;
    }

    public List<QuestionOption> getOptions() {
        return options;
    }

    public void setOptions(List<QuestionOption> options) {
        this.options = options;
    }
}
