package com.oceane.surveys.repositories;

import com.oceane.surveys.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySurvey_IdOrderByDisplayOrderAsc(Long surveyId);
}
