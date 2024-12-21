# Quiz Application

A simple REST API-based quiz application built with Spring Boot.

## Assumptions

1. Single User System
   - The application is pre-configured with a single user (john_doe)
   - No authentication/authorization is implemented
   - User ID 1 is used for all quiz sessions

2. Questions
   - Questions are pre-seeded in the H2 database
   - All questions are multiple choice with options A, B, C, and D
   - Questions are randomly selected from the database
   - The same question might appear multiple times in a session

3. Quiz Session
   - A user can have multiple active sessions
   - Sessions remain active indefinitely (no timeout implemented)
   - Users can answer unlimited questions in a session

4. Answer Submission
   - Answers should be submitted as single letters (A, B, C, or D)
   - Case-insensitive answer checking
   - No validation for duplicate answers to the same question

## API Endpoints

1. Start New Quiz Session
   ```
   POST /api/quiz/start
   
   Response: "Quiz session started successfully"
            HTTP Cookie with sessionID stored in it on path /api/quiz
   ```

2. Get Random Question
   ```
   GET /api/quiz/question

   Response: { 
     "questionId": 1,
     "questionText": "Sample question",
     "a": "Option A",
     "b": "Option B",
     "c": "Option C",
     "d": "Option D"
   }
   ```

3. Submit Answer
   ```
   POST /api/quiz/submit
   Request Body: {
     "questionId": 1,
     "answer": "A"
   }

   Response: "Correct answer!" or "Wrong answer!"

4. Summary
    ```
    GET /api/quiz/summary

    Response: {
    "totalQuestions": 5,
    "correctAnswers": 4,
    "incorrectAnswers": 1,
    "answers": [
        {
            "questionText": "What is 2 + 2?",
            "submittedAnswer": "b",
            "correct": true
        },
        // ... more answers
    ]
    }