package com.oceane.surveys.repositories;

import com.oceane.surveys.entities.Survey;
import com.oceane.surveys.entities.SurveyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findByStatus(SurveyStatus status);
    List<Survey> findByCreationDateAfter(LocalDateTime creationDate);
    List<Survey> findByTitleContainingIgnoreCase(String title);
    long countByStatus(SurveyStatus status);
}
