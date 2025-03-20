package com.oceane.surveys.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 */
@AllArgsConstructor
@Data
@Entity
@Table(name = "QUESTIONOPTION")
public class QuestionOption {
    /**
     *
     */
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    public Long questionoptionId;

    /**
     * Question parente
     */
    @Column(name = "QUESTION_ID")
    public Long questionId;

    /**
     *  Texte de l'option
     */
    @Column(name = "TEXT")
    public String text;

    /**
     * Ordre d'affichage dans la liste des options
     */
    @Column(name = "DISPLAYORDER")
    public Long displayOrder;
}
