package com.oceane.surveys.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "answers")
public class Answer {
    /**
     *
     */
    @Id
    @Column(name = "answer_id")
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    /**
     * Date de création
     */
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    /**
     * Question parente
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    /**
     * Répondeur
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private Recipient recipient;

    /**
     * Réponse en texte
     */
    @Column(name = "text", nullable = false)
    private String text;

    /**
     * Réponse en rating
     */
    @Column(name = "rating", nullable = false)
    private Integer rating;

    /**
     * Option choisie (s'il y a)
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "answer_question_options",
            joinColumns = @JoinColumn(name = "answer_id"),
            inverseJoinColumns = @JoinColumn(name = "question_option_id")
    )
    private Set<QuestionOption> options = new HashSet<>();
}
