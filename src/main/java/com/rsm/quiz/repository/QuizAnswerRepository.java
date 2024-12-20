package com.rsm.quiz.repository;

import com.rsm.quiz.model.QuizAnswer;
import com.rsm.quiz.model.QuizSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {
    List<QuizAnswer> findBySession(QuizSession session);
}
