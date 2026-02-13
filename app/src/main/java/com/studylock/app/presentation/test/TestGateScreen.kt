package com.studylock.app.presentation.test

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studylock.app.R
import com.studylock.app.domain.model.Question
import com.studylock.app.domain.model.QuestionType
import com.studylock.app.ui.theme.Primary
import com.studylock.app.ui.theme.Success
import kotlinx.coroutines.delay

data class TestState(
    val currentQuestionIndex: Int,
    val answers: Map<Int, String>,
    val timeRemaining: Int,
    val isComplete: Boolean,
    val score: Int,
    val passed: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestGateScreen(
    onTestPassed: () -> Unit,
    onTestFailed: () -> Unit
) {
    var testState by remember {
        mutableStateOf(
            TestState(
                currentQuestionIndex = 0,
                answers = emptyMap(),
                timeRemaining = 30 * 10, // 30 seconds per question, 10 questions
                isComplete = false,
                score = 0,
                passed = false
            )
        )
    }

    // Sample questions
    val questions = listOf(
        Question(
            id = "1",
            subject = "Mathematics",
            topic = "Algebra",
            grade = 10,
            questionText = "Solve for x: 2x + 5 = 15",
            questionType = QuestionType.MCQ,
            options = listOf(
                Question.Option("x = 5", true),
                Question.Option("x = 7", false),
                Question.Option("x = 10", false),
                Question.Option("x = 3", false)
            ),
            correctAnswer = "x = 5",
            explanation = "Subtract 5 from both sides: 2x = 10, then divide by 2: x = 5",
            com.studylock.app.domain.model.Difficulty.EASY
        ),
        Question(
            id = "2",
            subject = "Mathematics",
            topic = "Algebra",
            grade = 10,
            questionText = "What is the slope of the line y = 3x + 7?",
            questionType = QuestionType.MCQ,
            options = listOf(
                Question.Option("3", true),
                Question.Option("7", false),
                Question.Option("-3", false),
                Question.Option("0", false)
            ),
            correctAnswer = "3",
            explanation = "In the equation y = mx + b, m is the slope. Here, m = 3.",
            com.studylock.app.domain.model.Difficulty.EASY
        ),
        Question(
            id = "3",
            subject = "Mathematics",
            topic = "Algebra",
            grade = 10,
            questionText = "Simplify: (x + 3)(x - 2)",
            questionType = QuestionType.MCQ,
            options = listOf(
                Question.Option("x² + x - 6", true),
                Question.Option("x² + 5x - 6", false),
                Question.Option("x² - x + 6", false),
                Question.Option("x² + x + 6", false)
            ),
            correctAnswer = "x² + x - 6",
            explanation = "Using FOIL: x*x + x*(-2) + 3*x + 3*(-2) = x² - 2x + 3x - 6 = x² + x - 6",
            com.studylock.app.domain.model.Difficulty.MEDIUM
        )
    )

    val currentQuestion = questions.getOrNull(testState.currentQuestionIndex)

    // Timer countdown
    LaunchedEffect(testState.timeRemaining) {
        if (!testState.isComplete && testState.timeRemaining > 0) {
            delay(1000)
            testState = testState.copy(timeRemaining = testState.timeRemaining - 1)
        } else if (testState.timeRemaining == 0 && !testState.isComplete) {
            // Time's up, submit current answer
            currentQuestion?.let { submitAnswer(testState, it, "") }
        }
    }

    Scaffold { paddingValues ->
        when {
            !testState.isComplete && currentQuestion != null -> {
                // Test Screen
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Progress Bar
                    LinearProgressIndicator(
                        progress = { (testState.currentQuestionIndex + 1).toFloat() / questions.size },
                        modifier = Modifier.fillMaxWidth(),
                        color = Primary
                    )

                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.question_number, testState.currentQuestionIndex + 1, questions.size),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = null,
                                tint = if (testState.timeRemaining < 10) Color.Red else Primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "${testState.timeRemaining}s",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (testState.timeRemaining < 10) Color.Red else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Question
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.test_gate),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = currentQuestion.questionText,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Options
                        currentQuestion.options?.let { options ->
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                options.forEachIndexed { index, option ->
                                    val isSelected = testState.answers[testState.currentQuestionIndex] == option.text
                                    OptionCard(
                                        text = option.text,
                                        isSelected = isSelected,
                                        onClick = {
                                            testState = testState.copy(
                                                answers = testState.answers.toMutableMap().apply {
                                                    put(testState.currentQuestionIndex, option.text)
                                                }
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Navigation buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (testState.currentQuestionIndex > 0) {
                                        testState = testState.copy(
                                            currentQuestionIndex = testState.currentQuestionIndex - 1
                                        )
                                    }
                                },
                                enabled = testState.currentQuestionIndex > 0,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(stringResource(R.string.previous))
                            }

                            Button(
                                onClick = {
                                    val answer = testState.answers[testState.currentQuestionIndex] ?: ""
                                    if (answer.isNotBlank()) {
                                        moveToNextQuestion(testState, currentQuestion, answer, questions.size)
                                    }
                                },
                                enabled = testState.answers.containsKey(testState.currentQuestionIndex),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    if (testState.currentQuestionIndex == questions.size - 1) {
                                        "Submit"
                                    } else {
                                        stringResource(R.string.next)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            testState.isComplete -> {
                // Result Screen
                TestResultScreen(
                    score = testState.score,
                    passed = testState.passed,
                    questions = questions,
                    answers = testState.answers,
                    onUnlockPhone = onTestPassed,
                    onRetry = onTestFailed
                )
            }
        }
    }
}

fun submitAnswer(
    currentState: TestState,
    question: Question,
    answer: String
): TestState {
    val isCorrect = answer == question.correctAnswer
    val newAnswers = currentState.answers.toMutableMap().apply {
        put(currentState.currentQuestionIndex, answer)
    }

    return if (currentState.currentQuestionIndex < 2) { // Using sample size of 3
        currentState.copy(
            currentQuestionIndex = currentState.currentQuestionIndex + 1,
            answers = newAnswers,
            timeRemaining = 30 // Reset timer for next question
        )
    } else {
        // Test complete, calculate score
        val score = newAnswers.count { (index, ans) ->
            questions[index].correctAnswer == ans
        }
        val passed = score >= (questions.size * 0.8).toInt()

        currentState.copy(
            answers = newAnswers,
            isComplete = true,
            score = score,
            passed = passed
        )
    }
}

fun moveToNextQuestion(
    currentState: TestState,
    question: Question,
    answer: String,
    totalQuestions: Int
): TestState {
    if (currentState.currentQuestionIndex < totalQuestions - 1) {
        return currentState.copy(
            currentQuestionIndex = currentState.currentQuestionIndex + 1,
            timeRemaining = 30 // Reset timer for next question
        )
    } else {
        // Test complete, calculate score
        val score = currentState.answers.count { (index, ans) ->
            questions[index].correctAnswer == ans
        }
        val passed = score >= (totalQuestions * 0.8).toInt()

        return currentState.copy(
            isComplete = true,
            score = score,
            passed = passed
        )
    }
}

@Composable
fun OptionCard(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, Primary)
        } else null,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Primary else MaterialTheme.colorScheme.onSurface
            )
            RadioButton(
                selected = isSelected,
                onClick = onClick
            )
        }
    }
}

// Sample questions list for the function references
val questions = listOf(
    Question(
        id = "1",
        subject = "Mathematics",
        topic = "Algebra",
        grade = 10,
        questionText = "Solve for x: 2x + 5 = 15",
        questionType = QuestionType.MCQ,
        options = listOf(
            Question.Option("x = 5", true),
            Question.Option("x = 7", false),
            Question.Option("x = 10", false),
            Question.Option("x = 3", false)
        ),
        correctAnswer = "x = 5",
        explanation = "Subtract 5 from both sides: 2x = 10, then divide by 2: x = 5",
        com.studylock.app.domain.model.Difficulty.EASY
    )
)

@Composable
fun TestResultScreen(
    score: Int,
    passed: Boolean,
    questions: List<Question>,
    answers: Map<Int, String>,
    onUnlockPhone: () -> Unit,
    onRetry: () -> Unit
) {
    val percentage = ((score.toFloat() / questions.size) * 100).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon
        Icon(
            if (passed) Icons.Default.CheckCircle else Icons.Default.Cancel,
            contentDescription = null,
            tint = if (passed) Success else MaterialTheme.colorScheme.error,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Result text
        Text(
            text = if (passed) stringResource(R.string.you_passed) else stringResource(R.string.you_failed),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = if (passed) Success else MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "$percentage%",
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(R.string.correct, score, questions.size),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (passed) {
            // XP earned
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Primary.copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, tint = Primary)
                    Text(
                        text = stringResource(R.string.xp_awarded, 100 + (percentage - 80) * 10),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Unlock button
            Button(
                onClick = onUnlockPhone,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text(
                    text = stringResource(R.string.unlock_phone),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            // Penalty message
            Text(
                text = stringResource(R.string.phone_locked_for, 10),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Retry button (disabled initially)
            Button(
                onClick = onRetry,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                enabled = false, // TODO: Enable after penalty time
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    text = stringResource(R.string.ill_try_again),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Review answers button
        OutlinedButton(
            onClick = { /* Show answer review */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.review_answers))
        }
    }
}
