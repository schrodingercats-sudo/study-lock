package com.studylock.app.presentation.study

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.studylock.app.domain.model.TimerMode
import com.studylock.app.ui.theme.Primary

data class Subject(
    val name: String,
    val icon: String,
    val color: Color
)

data class Topic(
    val name: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyModeScreen(
    onStartSession: () -> Unit,
    onBack: () -> Unit
) {
    var selectedSubject by remember { mutableStateOf<Subject?>(null) }
    var selectedTopic by remember { mutableStateOf<Topic?>(null) }
    var timerMode by remember { mutableStateOf(TimerMode.CLASSIC) }
    var customStudyDuration by remember { mutableStateOf(25) }
    var customBreakDuration by remember { mutableStateOf(5) }
    var cycles by remember { mutableStateOf(1) }

    val subjects = listOf(
        Subject("Mathematics", "📐", Color(0xFFE3F2FD)),
        Subject("Physics", "⚡", Color(0xFFFFF3E0)),
        Subject("Chemistry", "🧪", Color(0xFFE8F5E9)),
        Subject("Biology", "🧬", Color(0xFFFCE4EC)),
        Subject("English", "📚", Color(0xFFF3E5F5)),
        Subject("History", "🏛️", Color(0xFFFFEBEE))
    )

    val topics = selectedSubject?.let { subject ->
        when (subject.name) {
            "Mathematics" -> listOf(
                Topic("Algebra"),
                Topic("Calculus"),
                Topic("Geometry"),
                Topic("Trigonometry")
            )
            "Physics" -> listOf(
                Topic("Mechanics"),
                Topic("Electricity"),
                Topic("Optics"),
                Topic("Thermodynamics")
            )
            else -> listOf(Topic("General"))
        }
    } ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Start Study Session") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Subject Selection
            item {
                Column {
                    Text(
                        text = stringResource(R.string.select_subject),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(subjects) { subject ->
                            SubjectCard(
                                subject = subject,
                                isSelected = selectedSubject == subject,
                                onClick = {
                                    selectedSubject = subject
                                    selectedTopic = null
                                }
                            )
                        }
                    }
                }
            }

            // Topic Selection
            if (selectedSubject != null) {
                item {
                    Column {
                        Text(
                            text = stringResource(R.string.select_topic),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            topics.forEach { topic ->
                                TopicCard(
                                    topic = topic,
                                    isSelected = selectedTopic == topic,
                                    onClick = { selectedTopic = topic }
                                )
                            }
                        }
                    }
                }
            }

            // Timer Configuration
            if (selectedTopic != null) {
                item {
                    Column {
                        Text(
                            text = stringResource(R.string.timer_mode),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        TimerMode.values().forEach { mode ->
                            TimerModeOption(
                                mode = mode,
                                isSelected = timerMode == mode,
                                onClick = { timerMode = mode }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        if (timerMode == TimerMode.CUSTOM) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Study Duration (min)",
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                    Slider(
                                        value = customStudyDuration.toFloat(),
                                        onValueChange = { customStudyDuration = it.toInt() },
                                        valueRange = 10f..120f,
                                        steps = 110
                                    )
                                    Text(
                                        text = "$customStudyDuration min",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Break Duration (min)",
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                    Slider(
                                        value = customBreakDuration.toFloat(),
                                        onValueChange = { customBreakDuration = it.toInt() },
                                        valueRange = 5f..30f,
                                        steps = 25
                                    )
                                    Text(
                                        text = "$customBreakDuration min",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(R.string.number_of_cycles),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            (1..8).forEach { cycleCount ->
                                FilterChip(
                                    selected = cycles == cycleCount,
                                    onClick = { cycles = cycleCount },
                                    label = { Text("$cycleCount") }
                                )
                            }
                        }
                    }
                }
            }

            // Start Button
            item {
                Button(
                    onClick = onStartSession,
                    enabled = selectedSubject != null && selectedTopic != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        disabledContainerColor = Primary.copy(alpha = 0.3f)
                    )
                ) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.start_session),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SubjectCard(
    subject: Subject,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Primary else subject.color
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, Primary)
        } else null
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = subject.icon,
                fontSize = 40.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subject.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun TopicCard(
    topic: Topic,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Primary else MaterialTheme.colorScheme.surface
        ),
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
                text = topic.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun TimerModeOption(
    mode: TimerMode,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val label = when (mode) {
        TimerMode.CLASSIC -> stringResource(R.string.classic_pomodoro)
        TimerMode.EXTENDED -> stringResource(R.string.extended_pomodoro)
        TimerMode.CUSTOM -> stringResource(R.string.custom_mode)
        TimerMode.EXAM -> stringResource(R.string.exam_mode)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Primary else MaterialTheme.colorScheme.surface
        ),
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
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
            RadioButton(
                selected = isSelected,
                onClick = onClick
            )
        }
    }
}
