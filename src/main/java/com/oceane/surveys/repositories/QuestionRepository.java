package com.oceane.surveys.repositories;

import com.oceane.surveys.entities.Question;
import com.oceane.surveys.entities.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySurveyId(Long surveyId);
    List<Question> findBySurveyIdAndType(Long surveyId, QuestionType type);
    List<Question> findBySurveyIdAndRequiredTrue(Long surveyId);
    void deleteBySurveyId(Long surveyId);
    long countBySurveyId(Long surveyId);
    List<Question> findBySurveyIdOrderByDisplayOrderAsc(Long surveyId);
}
