package com.rsm.quiz.dto;

import lombok.Data;

@Data
public class AnswerRequest {
    private Long sessionId;
    private Long questionId;
    private String answer;
}
