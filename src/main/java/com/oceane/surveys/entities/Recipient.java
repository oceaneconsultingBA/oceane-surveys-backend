package com.oceane.surveys.entities;

import jakarta.persistence.*;
import lombok.*;

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
    @ManyToMany(mappedBy = "recipients", fetch = FetchType.LAZY)
    private Set<Survey> surveys = new HashSet<>();

}
