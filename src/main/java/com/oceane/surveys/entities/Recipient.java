package com.oceane.surveys.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "SURVEY")
public class Recipient {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    public Long recipientId;

    @Column(name = "EMAIL")
    public String email;

    @Column(name = "FIRSTNAME")
    public String firstname;

    @Column(name = "LASTNAME")
    public String lastname;

    @Column(name = "COMPANY")
    public String company;

    @Column(name = "TYPE")
    public String type;

    public Recipient(Long recipientId, String email, String firstname, String lastname, String company, String type) {
        this.recipientId = recipientId;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.company = company;
        this.type = type;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
