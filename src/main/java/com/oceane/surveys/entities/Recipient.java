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
@Table(name = "RECIPIENT")
public class Recipient {
    /**
     *
     */
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    public Long recipientId;

    /**
     * Email du destinataire
     */
    @Column(name = "EMAIL")
    public String email;

    /**
     * Prénom
     */
    @Column(name = "FIRSTNAME")
    public String firstname;

    /**
     * Nom
     */
    @Column(name = "LASTNAME")
    public String lastname;

    /**
     * Entreprise ou client
     */
    @Column(name = "COMPANY")
    public String company;

    /**
     * Type
     */
    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    public RecipientType type;
}
