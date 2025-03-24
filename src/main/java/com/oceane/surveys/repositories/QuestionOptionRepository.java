package com.oceane.surveys.repositories;

import com.oceane.surveys.entities.QuestionOption;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionOptionRepository extends CrudRepository<QuestionOption, Long> {
    List<QuestionOption> findByQuestionId(Long questionOptionId);
}
