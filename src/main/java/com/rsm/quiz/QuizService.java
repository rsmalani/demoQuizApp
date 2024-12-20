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
            
        QuizSession session = new QuizSession();
        session.setUser(user);
        session.setStartTime(LocalDateTime.now());
        session.setActive(true);
        
        session = sessionRepository.save(session);
        
        QuizSessionResponse response = new QuizSessionResponse();
        response.setSessionId(session.getId());
        response.setMessage("Quiz session started successfully");
        return response;
    }
    
    public QuestionResponse getRandomQuestion(Long sessionId) {
        QuizSession session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found"));
            
        if (!session.isActive()) {
            throw new RuntimeException("Session is not active");
        }
        
        Question question = questionRepository.findRandomQuestion();
        QuestionResponse response = new QuestionResponse();
        response.setQuestionId(question.getId());
        response.setQuestionText(question.getQuestionText());
        response.setOptionA(question.getOptionA());
        response.setOptionB(question.getOptionB());
        response.setOptionC(question.getOptionC());
        response.setOptionD(question.getOptionD());
        return response;
    }
    
    public String submitAnswer(AnswerRequest request) {
        QuizSession session = sessionRepository.findById(request.getSessionId())
            .orElseThrow(() -> new RuntimeException("Session not found"));
            
        if (!session.isActive()) {
            throw new RuntimeException("Session is not active");
        }
        
        Question question = questionRepository.findById(request.getQuestionId())
            .orElseThrow(() -> new RuntimeException("Question not found"));
            
        QuizAnswer answer = new QuizAnswer();
        answer.setSession(session);
        answer.setQuestion(question);
        answer.setSubmittedAnswer(request.getAnswer());
        answer.setCorrect(question.getCorrectOption().equalsIgnoreCase(request.getAnswer()));
        answer.setSubmissionTime(LocalDateTime.now());
        
        answerRepository.save(answer);
        
        return answer.isCorrect() ? "Correct answer!" : "Wrong answer!";
    }
    
    public QuizSummary getQuizSummary(Long sessionId) {
        QuizSession session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found"));
            
        List<QuizAnswer> answers = answerRepository.findBySession(session);
        
        QuizSummary summary = new QuizSummary();
        summary.setTotalQuestions(answers.size());
        summary.setCorrectAnswers((int) answers.stream().filter(QuizAnswer::isCorrect).count());
        summary.setIncorrectAnswers(answers.size() - summary.getCorrectAnswers());
        
        List<QuizSummary.AnswerDetail> answerDetails = new ArrayList<>();
        for (QuizAnswer answer : answers) {
            QuizSummary.AnswerDetail detail = new QuizSummary.AnswerDetail();
            detail.setQuestionText(answer.getQuestion().getQuestionText());
            detail.setSubmittedAnswer(answer.getSubmittedAnswer());
            detail.setCorrect(answer.isCorrect());
            answerDetails.add(detail);
        }
        summary.setAnswers(answerDetails);
        
        return summary;
    }
}
