package com.oceane.surveys.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
@Table(name = "surveys")
public class Survey {
    /**
     *
     */
    @Id
    @Column(name = "survey_id")
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    public Long surveyId;

    /**
     * Titre de l'enquête
     */
    @Column(name = "title", nullable = false)
    public String title;

    /**
     * Description détaillée
     */
    @Column(name = "description", length = 1000)
    public String description;

    /**
     * Date de création
     */
    @Column(name = "creation_date", nullable = false)
    public LocalDateTime creationDate;

    /**
     * Date de dernière modification
     */
    @Column(name = "last_modified_date")
    public LocalDateTime lastModifiedDate;

    /**
     * Statut de l'enquête
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    public SurveyStatus status;

    /**
     * Questions associées
     */
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    /**
     * Personnes
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "survey_recipients",
            joinColumns = @JoinColumn(name = "survey_id"),
            inverseJoinColumns = @JoinColumn(name = "recipient_id")
    )
    private Set<Recipient> recipients = new HashSet<>();

    // Helper methods
    public void addQuestion(Question question) {
        questions.add(question);
        question.setSurvey(this);
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
        question.setSurvey(null);
    }

    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
        lastModifiedDate = LocalDateTime.now();
        if (status == null) {
            status = SurveyStatus.DRAFT;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifiedDate = LocalDateTime.now();
    }
}