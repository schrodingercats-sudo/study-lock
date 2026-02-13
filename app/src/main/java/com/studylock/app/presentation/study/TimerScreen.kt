package com.studylock.app.presentation.study

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studylock.app.R
import com.studylock.app.ui.theme.Primary
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    onSessionComplete: (String) -> Unit,
    onBack: () -> Unit
) {
    var remainingTime by remember { mutableStateOf(25 * 60) } // 25 minutes in seconds
    var isPaused by remember { mutableStateOf(false) }
    var cycleCount by remember { mutableStateOf(0) }
    var isBreak by remember { mutableStateOf(false) }
    var totalCycles by remember { mutableStateOf(1) }

    val totalTime = if (isBreak) 5 * 60 else 25 * 60
    val progress = 1f - (remainingTime.toFloat() / totalTime.toFloat())

    LaunchedEffect(remainingTime, isPaused) {
        if (!isPaused && remainingTime > 0) {
            delay(1000)
            remainingTime--
        } else if (remainingTime == 0) {
            if (isBreak) {
                // Break over, start next study cycle
                isBreak = false
                remainingTime = 25 * 60
                cycleCount++
            } else {
                // Study session complete
                if (cycleCount < totalCycles - 1) {
                    // Start break
                    isBreak = true
                    remainingTime = 5 * 60
                } else {
                    // All cycles complete
                    onSessionComplete("session_id")
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Focus Timer") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Subject and Topic
            Text(
                text = "Mathematics - Algebra",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Timer Ring
            Box(
                modifier = Modifier.size(300.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 16.dp.toPx()
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.minDimension / 2 - strokeWidth

                    // Background circle
                    drawArc(
                        color = Color(0xFFE0E0E0),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Progress arc
                    drawArc(
                        color = if (isBreak) Color(0xFF00C853) else Primary,
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = formatTime(remainingTime),
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = if (isBreak) "Break Time" else stringResource(R.string.focus_time),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pause/Resume Button
                FilledTonalButton(
                    onClick = { isPaused = !isPaused },
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(
                        if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Sound Button
                FilledTonalButton(
                    onClick = { /* Toggle sound */ },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        Icons.Default.VolumeUp,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Cycle indicator
            Text(
                text = "Cycle ${cycleCount + 1} of $totalCycles",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )

            // Pause limit indicator
            if (isPaused) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "2 pauses remaining this session",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}
