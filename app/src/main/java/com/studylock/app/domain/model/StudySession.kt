package com.studylock.app.domain.model

enum class TimerMode {
    CLASSIC, // 25/5
    EXTENDED, // 50/10
    CUSTOM,
    EXAM
}

enum class SessionStatus {
    IN_PROGRESS,
    COMPLETED,
    ABANDONED
}

data class StudySession(
    val id: String,
    val userId: String,
    val subject: String,
    val topic: String,
    val timerMode: TimerMode,
    val studyDurationMinutes: Int,
    val breakDurationMinutes: Int,
    val cyclesCompleted: Int,
    val startedAt: Long,
    val completedAt: Long? = null,
    val status: SessionStatus = SessionStatus.IN_PROGRESS,
    val ambientSound: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class SessionConfig(
    val timerMode: TimerMode = TimerMode.CLASSIC,
    val studyDuration: Int = 25,
    val breakDuration: Int = 5,
    val cycles: Int = 1
)
