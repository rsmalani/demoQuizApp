package com.rsm.quiz.dto;

public record QuestionResponse(
    Long questionId,
    String questionText,
    String a,
    String b,
    String c,
    String d
) {
}
