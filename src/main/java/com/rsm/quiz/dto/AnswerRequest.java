package com.rsm.quiz.dto;

public record AnswerRequest(
    Long sessionId,
    Long questionId,
    String answer
) {
    
}
