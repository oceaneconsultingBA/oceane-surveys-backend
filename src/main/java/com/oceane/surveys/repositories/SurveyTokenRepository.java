package com.oceane.surveys.repositories;

import com.oceane.surveys.entities.SurveyToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyTokenRepository extends JpaRepository<SurveyToken, Long> {
    Optional<SurveyToken> findByToken(String token);
    List<SurveyToken> findBySurveyIdAndRecipientId(Long surveyId, Long recipientId);
    boolean existsByToken(String token);
}
