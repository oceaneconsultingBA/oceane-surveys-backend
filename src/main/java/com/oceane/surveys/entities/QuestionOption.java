package com.oceane.surveys.entities;

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
@Table(name = "question_options")
public class QuestionOption {
    /**
     *
     */
    @Id
    @Column(name = "question_option_id")
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    public Long questionOptionId;

    /**
     * Question parente
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "question_id")
    private Question question;

    /**
     *  Texte de l'option
     */
    @Column(name = "text")
    public String text;

    /**
     * Ordre d'affichage dans la liste des options
     */
    @Column(name = "display_order", nullable = false)
    public int displayOrder;
}
