package com.oceane.surveys.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 *
 */
@NoArgsConstructor
@AllArgsConstructor
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
    private Long id;

    /**
     * Titre de l'enquête
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Description détaillée
     */
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * Date de création
     */
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    /**
     * Date de dernière modification
     */
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    /**
     * Statut de l'enquête
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SurveyStatus status;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public SurveyStatus getStatus() {
        return status;
    }

    public void setStatus(SurveyStatus status) {
        this.status = status;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Set<Recipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<Recipient> recipients) {
        this.recipients = recipients;
    }
}