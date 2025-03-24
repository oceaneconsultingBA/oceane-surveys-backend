package com.oceane.surveys.repositories;

import com.oceane.surveys.entities.Survey;
import com.oceane.surveys.entities.SurveyStatus;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SurveyRepository extends CrudRepository<Survey, Long> {
    List<Survey> findByStatus(SurveyStatus status);
    List<Survey> findByCreationDateAfter(LocalDateTime creationDate);
    List<Survey> findByTitleContainingIgnoreCase(String title);
    long countByStatus(SurveyStatus status);
}
