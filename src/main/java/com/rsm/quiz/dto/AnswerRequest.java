package com.rsm.quiz.dto;

public record AnswerRequest(
    Long questionId,
    String answer
) {
    
}
