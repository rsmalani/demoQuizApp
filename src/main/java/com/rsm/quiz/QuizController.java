package com.rsm.quiz;

import com.rsm.quiz.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {
    
    @Autowired
    private QuizService quizService;
    
    @PostMapping("/start")
    public ResponseEntity<QuizSessionResponse> startQuiz() {
        return ResponseEntity.ok(quizService.startNewSession());
    }
    
    @GetMapping("/question")
    public ResponseEntity<QuestionResponse> getQuestion(@RequestParam Long sessionId) {
        return ResponseEntity.ok(quizService.getRandomQuestion(sessionId));
    }
    
    @PostMapping("/submit")
    public ResponseEntity<String> submitAnswer(@RequestBody AnswerRequest request) {
        return ResponseEntity.ok(quizService.submitAnswer(request));
    }
    
    @GetMapping("/summary")
    public ResponseEntity<QuizSummary> getQuizSummary(@RequestParam Long sessionId) {
        return ResponseEntity.ok(quizService.getQuizSummary(sessionId));
    }
}