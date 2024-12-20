package com.rsm.quiz;

import com.rsm.quiz.dto.*;
import com.rsm.quiz.model.*;
import com.rsm.quiz.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private QuizSessionRepository sessionRepository;
    
    @Autowired
    private QuizAnswerRepository answerRepository;
    
    public QuizSessionResponse startNewSession() {
        AppUser user = userRepository.findById(1L)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        var session = new QuizSession();
        session.setUser(user);
        session.setStartTime(LocalDateTime.now());
        session.setActive(true);
        session = sessionRepository.save(session);
        
        var response = new QuizSessionResponse(session.getId(), "Quiz session started successfully");
        return response;
    }
    
    public QuestionResponse getRandomQuestion(Long sessionId) {
        QuizSession session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found"));
            
        if (!session.isActive()) {
            throw new RuntimeException("Session is not active");
        }
        
        var question = questionRepository.findRandomQuestion();
        var response = new QuestionResponse(
            question.getId(),
            question.getQuestionText(),
            question.getOptionA(),
            question.getOptionB(),
            question.getOptionC(),
            question.getOptionD());
        return response;
    }
    
    public String submitAnswer(AnswerRequest request) {
        QuizSession session = sessionRepository.findById(request.sessionId())
            .orElseThrow(() -> new RuntimeException("Session not found"));
            
        if (!session.isActive()) {
            throw new RuntimeException("Session is not active");
        }
        
        var question = questionRepository.findById(request.questionId())
            .orElseThrow(() -> new RuntimeException("Question not found"));
            
        var answer = new QuizAnswer();
        answer.setSession(session);
        answer.setQuestion(question);
        answer.setSubmittedAnswer(request.answer());
        answer.setCorrect(question.getCorrectOption().equalsIgnoreCase(request.answer()));
        answer.setSubmissionTime(LocalDateTime.now());
        answerRepository.save(answer);
        
        return answer.isCorrect() ? "Correct answer!" : "Wrong answer!";
    }
    
    public QuizSummary getQuizSummary(Long sessionId) {
        QuizSession session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found"));
            
        List<QuizAnswer> answers = answerRepository.findBySession(session);
        
        List<QuizSummary.AnswerDetail> answerDetails = new ArrayList<>();
        for (QuizAnswer answer : answers) {
            QuizSummary.AnswerDetail detail = new QuizSummary.AnswerDetail(
                answer.getQuestion().getQuestionText(),
                answer.getSubmittedAnswer(),
                answer.isCorrect());
                
            answerDetails.add(detail);
        }

        int correctAnswers = (int) answers.stream().filter(QuizAnswer::isCorrect).count();
        QuizSummary summary = new QuizSummary(
            answers.size(),
            correctAnswers,
            answers.size() - correctAnswers,
            answerDetails);
        
        return summary;
    }
}
