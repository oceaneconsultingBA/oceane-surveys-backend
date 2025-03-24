package com.oceane.surveys.repositories;

import com.oceane.surveys.entities.Recipient;
import com.oceane.surveys.entities.RecipientType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecipientRepository extends CrudRepository<Recipient, Long> {
    List<Recipient> findByEmail(String email);
    List<Recipient> findByType(RecipientType type);
    List<Recipient> findBySurveysId(String surveysId);
}
