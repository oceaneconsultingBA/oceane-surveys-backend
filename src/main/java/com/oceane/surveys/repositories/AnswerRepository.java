package com.oceane.surveys.repositories;

import com.oceane.surveys.entities.Answer;
import com.oceane.surveys.entities.Question;
import com.oceane.surveys.entities.SurveyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("select answer from Answer answer where answer.question.survey.id = :surveyId order by answer.question.displayOrder asc")
    List<Answer> findBySurvey_IdOrderByDisplayOrderAsc(Long surveyId);

    @Query("select answer from Answer answer where answer.question.survey.id = :surveyId and answer.recipient.id = :recipientId order by answer.question.displayOrder asc")
    List<Answer> findBySurvey_IdAndRecipient_IdOrderByDisplayOrderAsc(Long surveyId, Long recipientId);

    long count();
}
