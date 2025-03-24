package com.oceane.surveys.repositories;

import com.oceane.surveys.entities.Recipient;
import com.oceane.surveys.entities.RecipientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, Long> {
    Optional<Recipient> findByEmail(String email);
    List<Recipient> findByType(RecipientType type);
    List<Recipient> findBySurveysId(Long surveysId);
    boolean existsByEmail(String email);
}
