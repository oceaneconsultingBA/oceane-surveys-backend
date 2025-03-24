package com.oceane.surveys.repositories;

import com.oceane.surveys.entities.Question;
import com.oceane.surveys.entities.QuestionType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    List<Question> findBySurveyId(Long surveyId);
    List<Question> findBySurveyIdAndType(Long surveyId, QuestionType type);
    List<Question> findBySurveyIdAndRequiredTrue(Long surveyId);
}
