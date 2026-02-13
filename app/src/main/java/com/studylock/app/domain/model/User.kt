package com.studylock.app.domain.model

enum class UserRole {
    STUDENT,
    TEACHER
}

data class User(
    val id: String,
    val email: String,
    val fullName: String,
    val role: UserRole,
    val profilePhotoUrl: String? = null,
    val grade: Int? = null, // For students
    val institutionName: String? = null,
    val subjects: List<String> = emptyList(),
    val parentEmail: String? = null,
    val xp: Int = 0,
    val level: Int = 1,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val hillPosition: Float = 0f,
    val reportFrequency: ReportFrequency = ReportFrequency.WEEKLY,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class ReportFrequency {
    WEEKLY,
    MONTHLY
}
