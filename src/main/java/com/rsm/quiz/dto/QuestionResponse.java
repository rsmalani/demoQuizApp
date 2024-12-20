package com.rsm.quiz.dto;

public record QuestionResponse(
    Long questionId,
    String questionText,
    String optionA,
    String optionB,
    String optionC,
    String optionD
) {
}
