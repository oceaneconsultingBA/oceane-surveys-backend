package com.oceane.surveys.entities;

import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 */
@AllArgsConstructor
@Data
@Entity
@Table(name = "QUESTION")
public class Question {
    /**
     *
     */
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    public Long questionId;

    /**
     * Enquête parente
     */
    @Column(name = "SURVEY_ID")
    public Long surveyId;

    /**
     * Libellé de la question
     */
    @Column(name = "TEXT")
    public String text;

    /**
     * Type de question
     */
    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    public QuestionType type;

    /**
     * Indique si la réponse est obligatoire
     */
    @Column(name = "REQUIRED")
    public Long required;

    /**
     * Ordre d'affichage dans l'enquête
     */
    @Column(name = "DISPLAYORDER")
    public Integer displayOrder;

    /**
     * Logique conditionnelle d'affichage (format JSON, optionnel)
     */
    @Column(name = "CONDITIONALLOGIC")
    public String conditionalLogic;

    /**
     * Options de réponse (pour questions à choix)
     */
    @OneToMany(mappedBy="questionId")
    private List<QuestionOption> questionOptions;
}
