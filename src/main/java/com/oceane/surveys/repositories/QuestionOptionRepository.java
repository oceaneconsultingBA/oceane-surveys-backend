package com.oceane.surveys.repositories;

import com.oceane.surveys.entities.Question;
import com.oceane.surveys.entities.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {
    List<QuestionOption> findByQuestionId(Long questionId);
    void deleteByQuestionId(Long questionId);
    List<QuestionOption> findByQuestionIdOrderByDisplayOrderAsc(Long questionId);
}
