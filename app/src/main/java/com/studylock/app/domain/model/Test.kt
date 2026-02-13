package com.studylock.app.domain.model

enum class QuestionType {
    MCQ,
    TRUE_FALSE,
    FILL_BLANK
}

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD
}

data class Question(
    val id: String,
    val subject: String,
    val topic: String,
    val grade: Int,
    val questionText: String,
    val questionType: QuestionType,
    val options: List<Option>? = null, // For MCQ
    val correctAnswer: String,
    val explanation: String,
    val difficulty: Difficulty
)

data class Option(
    val text: String,
    val isCorrect: Boolean
)

data class TestResult(
    val id: String,
    val userId: String,
    val sessionId: String,
    val subject: String,
    val topic: String,
    val questionCount: Int,
    val correctAnswers: Int,
    val scorePercentage: Float,
    val passed: Boolean,
    val timeTakenSeconds: Int,
    val integrityScore: Int,
    val integrityFlags: List<String>,
    val xpAwarded: Int,
    val attemptNumber: Int,
    val completedAt: Long
)

data class TestAnswer(
    val questionId: String,
    val userAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean,
    val timeToAnswerSeconds: Float
)
