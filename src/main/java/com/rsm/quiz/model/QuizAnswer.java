package com.rsm.quiz.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class QuizAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private QuizSession session;
    
    @ManyToOne
    private Question question;
    
    private String submittedAnswer;
    private boolean correct;
    private LocalDateTime submissionTime;
}
