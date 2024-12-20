package com.rsm.quiz.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuizSummary {
    private int totalQuestions;
    private int correctAnswers;
    private int incorrectAnswers;
    private List<AnswerDetail> answers;
    
    @Data
    public static class AnswerDetail {
        private String questionText;
        private String submittedAnswer;
        private boolean correct;
    }
}
