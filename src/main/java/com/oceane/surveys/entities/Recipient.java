package com.oceane.surveys.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@NoArgsConstructor
@AllArgsConstructor
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
    private Long id;

    /**
     * Email du destinataire
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Prénom
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * Nom
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * Entreprise ou client
     */
    @Column(name = "company")
    private String company;

    /**
     * Type
     */
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RecipientType type;

    /**
     * Enquête associée à la personne
     */
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Survey> surveys = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public RecipientType getType() {
        return type;
    }

    public void setType(RecipientType type) {
        this.type = type;
    }

    public Set<Survey> getSurveys() {
        return surveys;
    }

    public void setSurveys(Set<Survey> surveys) {
        this.surveys = surveys;
    }
}
