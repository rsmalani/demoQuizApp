package com.rsm.quiz;

import com.rsm.quiz.dto.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {
    
    @Autowired
    private QuizService quizService;
    
    @PostMapping("/start")
    public ResponseEntity<String> startQuiz(HttpServletResponse response) {
        var quizSessionResponse = quizService.startNewSession();
        // Create a cookie to store the session ID
        Cookie cookie = new Cookie("quizSessionId", quizSessionResponse.sessionId().toString());
        cookie.setHttpOnly(true); // Prevent access to cookie from JavaScript
        cookie.setPath("/api/quiz"); // Scope of the cookie to quiz endpoints
        cookie.setMaxAge(3600); // 1 hour expiration (you can adjust)
        response.addCookie(cookie);

        return ResponseEntity.ok(quizSessionResponse.message());
        
    }
    
    @GetMapping("/question")
    public ResponseEntity<QuestionResponse> getQuestion(HttpServletRequest request) {
        Long sessionId = extractSessionIdFromCookie(request);
        var questionResponse = quizService.getRandomQuestion(sessionId);
        return ResponseEntity.ok(questionResponse);
    }
    
    @PostMapping("/submit")
    public ResponseEntity<String> submitAnswer(@RequestBody AnswerRequest request, HttpServletRequest httpRequest) {
        Long sessionId = extractSessionIdFromCookie(httpRequest);
        var submitAnswerResponse = quizService.submitAnswer(sessionId, request);
        return ResponseEntity.ok(submitAnswerResponse);
    }
    
    @GetMapping("/summary")
    public ResponseEntity<QuizSummary> getQuizSummary(HttpServletRequest request) {
        Long sessionId = extractSessionIdFromCookie(request);
        var summaryResponse = quizService.getQuizSummary(sessionId);
        return ResponseEntity.ok(summaryResponse);
    }

    private Long extractSessionIdFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("quizSessionId".equals(cookie.getName())) {
                    return Long.valueOf(cookie.getValue());
                }
            }
        }
        throw new RuntimeException("Session ID not found in cookie");
    }
}