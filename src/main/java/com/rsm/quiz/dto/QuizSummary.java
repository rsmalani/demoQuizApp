package com.rsm.quiz.dto;

import java.util.List;

public record QuizSummary (
    int totalQuestions,
    int correctAnswers,
    int incorrectAnswers,
    List<AnswerDetail> answers
    ) {
    public record AnswerDetail (
        String questionText,
        String submittedAnswer,
        boolean correct
    ){
    }
}
