package com.studylock.app.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studylock.app.R
import com.studylock.app.ui.theme.Primary
import kotlinx.coroutines.launch

data class OnboardingSlide(
    val title: Int,
    val subtitle: Int,
    val icon: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit
) {
    val slides = listOf(
        OnboardingSlide(
            title = R.string.onboarding_slide1_title,
            subtitle = R.string.onboarding_slide1_subtitle,
            icon = "🔒"
        ),
        OnboardingSlide(
            title = R.string.onboarding_slide2_title,
            subtitle = R.string.onboarding_slide2_subtitle,
            icon = "📚"
        ),
        OnboardingSlide(
            title = R.string.onboarding_slide3_title,
            subtitle = R.string.onboarding_slide3_subtitle,
            icon = "🏔️"
        )
    )

    val pagerState = rememberPagerState(pageCount = { slides.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                OnboardingSlideContent(slides[page])
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(slides.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Primary else Color.Gray
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Button(
                onClick = {
                    if (pagerState.currentPage < slides.size - 1) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onGetStarted()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text(
                    text = if (pagerState.currentPage == slides.size - 1) {
                        stringResource(R.string.get_started)
                    } else {
                        "Next"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun OnboardingSlideContent(slide: OnboardingSlide) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = slide.icon,
            fontSize = 120.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = stringResource(slide.title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(slide.subtitle),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}
