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
@Table(name = "recipients")
public class Recipient {
    /**
     *
     */
    @Id
    @Column(name = "recipient_id")
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    public Long recipientId;

    /**
     * Email du destinataire
     */
    @Column(name = "email", nullable = false, unique = true)
    public String email;

    /**
     * Prénom
     */
    @Column(name = "firstname")
    public String firstname;

    /**
     * Nom
     */
    @Column(name = "lastname")
    public String lastname;

    /**
     * Entreprise ou client
     */
    @Column(name = "company")
    public String company;

    /**
     * Type
     */
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    public RecipientType type;

    /**
     * Enquête associée à la personne
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "survey_recipients",
            joinColumns = @JoinColumn(name = "recipient_id"),
            inverseJoinColumns = @JoinColumn(name = "survey_id"))
    private Set<Survey> surveys = new HashSet<>();
}
