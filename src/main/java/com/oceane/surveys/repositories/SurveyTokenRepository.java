package com.oceane.surveys.repositories;

import com.oceane.surveys.entities.SurveyToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyTokenRepository extends JpaRepository<SurveyToken, Long> {
    Optional<SurveyToken> findByToken(String token);
}
