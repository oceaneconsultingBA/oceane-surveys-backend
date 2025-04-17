package com.oceane.surveys.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private Long id;

    /**
     * Question parente
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    /**
     *  Texte de l'option
     */
    @Column(name = "text", nullable = false)
    private String text;

    /**
     * Ordre d'affichage dans la liste des options
     */
    @Column(name = "display_order")
    private Integer displayOrder;

}
