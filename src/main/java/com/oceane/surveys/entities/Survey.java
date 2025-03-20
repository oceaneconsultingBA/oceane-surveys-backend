package com.oceane.surveys.entities;

import java.time.LocalDateTime;
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
@Table(name = "SURVEY")
public class Survey {
    /**
     *
     */
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    public Long surveyId;

    /**
     * Titre de l'enquête
     */
    @Column(name = "TITLE")
    public String title;

    /**
     * Description détaillée
     */
    @Column(name = "DESCRIPTION")
    public String description;

    /**
     * Date de création
     */
    @Column(name = "CREATIONDATE")
    public LocalDateTime creationDate;

    /**
     * Date de dernière modification
     */
    @Column(name = "LASTMODIFIEDDATE")
    public LocalDateTime lastModifiedDate;

    /**
     * Statut de l'enquête
     */
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    public SurveyStatus status;

    /**
     * Questions associées
     */
    @OneToMany(mappedBy="surveyId")
    private List<Question> questions;
}