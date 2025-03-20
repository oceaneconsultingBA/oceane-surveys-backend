package com.oceane.surveys.entities;

import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "SURVEY")
public class Survey {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    public Long surveyId;

    @Column(name = "TITLE")
    public String title;

    @Column(name = "DESCRIPTION")
    public String description;

    @Column(name = "CREATIONDATE")
    public java.sql.Date creationDate;

    @Column(name = "LASTMODIFIEDDATE")
    public java.sql.Date lastModifiedDate;

    @Column(name = "STATUS")
    public String status;

    @OneToMany(mappedBy="surveyId")
    private Set<Question> questions;
}
