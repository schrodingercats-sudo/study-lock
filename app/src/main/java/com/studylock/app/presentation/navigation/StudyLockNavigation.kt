package com.studylock.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.studylock.app.MainViewModel
import com.studylock.app.presentation.onboarding.OnboardingScreen
import com.studylock.app.presentation.auth.LoginScreen
import com.studylock.app.presentation.auth.RoleSelectionScreen
import com.studylock.app.presentation.auth.SignUpScreen
import com.studylock.app.presentation.home.HomeScreen
import com.studylock.app.presentation.study.StudyModeScreen
import com.studylock.app.presentation.study.TimerScreen
import com.studylock.app.presentation.test.TestGateScreen

@Composable
fun StudyLockNavigation(
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = viewModel()
) {
    val isAuthenticated by mainViewModel.isAuthenticated.collectAsState()

    val startDestination = if (isAuthenticated) {
        Screen.Home.route
    } else {
        Screen.Onboarding.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Onboarding
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onGetStarted = {
                    navController.navigate(Screen.RoleSelection.route)
                }
            )
        }

        // Role Selection
        composable(Screen.RoleSelection.route) {
            RoleSelectionScreen(
                onRoleSelected = { role ->
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        // Auth
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        // Main App
        composable(Screen.Home.route) {
            HomeScreen(
                onStartStudySession = {
                    navController.navigate(Screen.StudyMode.route)
                },
                onNavigateToQuizzes = { /* Navigate to quizzes */ },
                onNavigateToDoubts = { /* Navigate to doubts */ },
                onNavigateToLeaderboard = { /* Navigate to leaderboard */ },
                onNavigateToProfile = { /* Navigate to profile */ }
            )
        }

        // Study Mode
        composable(Screen.StudyMode.route) {
            StudyModeScreen(
                onStartSession = { /* Navigate to timer */ },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // Timer
        composable(Screen.Timer.route) {
            TimerScreen(
                onSessionComplete = { sessionId ->
                    navController.navigate(Screen.TestGate.createRoute(sessionId))
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // Test Gate
        composable(
            route = Screen.TestGate.route,
            arguments = listOf(
                navArgument("sessionId") { type = NavType.StringType }
            )
        ) {
            TestGateScreen(
                onTestPassed = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onTestFailed = {
                    // Stay on test result screen with retry option
                }
            )
        }
    }
}
