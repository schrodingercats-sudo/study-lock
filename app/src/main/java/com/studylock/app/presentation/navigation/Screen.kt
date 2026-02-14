package com.studylock.app.presentation.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object RoleSelection : Screen("role_selection")
    object Permission : Screen("permission")

    // Auth
    object Login : Screen("login")
    object SignUp : Screen("signup")

    // Main Student Flow
    object Home : Screen("home")
    object StudyMode : Screen("study_mode")
    object Timer : Screen("timer")
    object TestGate : Screen("test_gate/{sessionId}") {
        fun createRoute(sessionId: String) = "test_gate/$sessionId"
    }
    object TestResult : Screen("test_result/{testResultId}") {
        fun createRoute(testResultId: String) = "test_result/$testResultId"
    }

    // Quizzes
    object QuizLobby : Screen("quiz_lobby")
    object LiveQuiz : Screen("live_quiz/{quizId}") {
        fun createRoute(quizId: String) = "live_quiz/$quizId"
    }
    object QuizResult : Screen("quiz_result/{quizId}") {
        fun createRoute(quizId: String) = "quiz_result/$quizId"
    }

    // Doubts
    object PostDoubt : Screen("post_doubt")
    object DoubtChat : Screen("doubt_chat/{doubtId}") {
        fun createRoute(doubtId: String) = "doubt_chat/$doubtId"
    }

    // Progress & Analytics
    object Progress : Screen("progress")
    object Reports : Screen("reports")

    // Leaderboard
    object Leaderboard : Screen("leaderboard")

    // Profile & Settings
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object EditProfile : Screen("edit_profile")
}
