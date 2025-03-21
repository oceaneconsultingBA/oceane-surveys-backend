package com.oceane.surveys.entities;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
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
    public Long questionId;

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
    public String text;

    /**
     * Type de question
     */
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    public QuestionType type;

    /**
     * Indique si la réponse est obligatoire
     */
    @Column(name = "required", nullable = false)
    public Boolean required;

    /**
     * Ordre d'affichage dans l'enquête
     */
    @Column(name = "display_order", nullable = false)
    public int displayOrder;

    /**
     * Logique conditionnelle d'affichage (format JSON, optionnel)
     */
    @Column(name = "conditional_logic", columnDefinition = "TEXT")
    public String conditionalLogic;

    /**
     * Options de réponse (pour questions à choix)
     */
    @OneToMany(mappedBy = "questions", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOption> options = new ArrayList<>();
}
