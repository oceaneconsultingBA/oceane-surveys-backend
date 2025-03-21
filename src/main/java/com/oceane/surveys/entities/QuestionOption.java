package com.oceane.surveys.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "questionoptions")
public class QuestionOption {
    /**
     *
     */
    @Id
    @Column(name = "questionoption_id")
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    public Long questionoptionId;

    /**
     * Question parente
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "question_questionoption",
            joinColumns = @JoinColumn(name = "questionoption_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private Set<Question> questions = new HashSet<>();

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
