package com.victoria.recipes.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.victoria.recipes.mvi.ObserveEffects
import com.victoria.recipes.ui.BackgroundStyle
import com.victoria.recipes.ui.PrimaryButton
import com.victoria.recipes.ui.ScreenBackground
import com.victoria.recipes.ui.SoftCard
import kotlin.math.absoluteValue
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import recipesapp.shared.generated.resources.Res
import recipesapp.shared.generated.resources.ic_arrow_back
import recipesapp.shared.generated.resources.ic_arrow_forward
import recipesapp.shared.generated.resources.onboarding_back
import recipesapp.shared.generated.resources.onboarding_next
import recipesapp.shared.generated.resources.onboarding_skip
import recipesapp.shared.generated.resources.onboarding_start

@Composable
fun OnboardingScreen(
    onNavigateToAuth: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(initialPage = state.currentPage, pageCount = { state.pages.size })

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.onIntent(OnboardingIntent.PageChanged(page))
        }
    }

    ObserveEffects(viewModel.effects) { effect ->
        when (effect) {
            is OnboardingEffect.ScrollToPage -> pagerState.animateScrollToPage(effect.page)
            OnboardingEffect.NavigateToAuth -> onNavigateToAuth()
            OnboardingEffect.NavigateToHome -> onNavigateToHome()
        }
    }

    OnboardingContent(state = state, pagerState = pagerState, onIntent = viewModel::onIntent)
}

@Composable
private fun OnboardingContent(
    state: OnboardingState,
    pagerState: PagerState,
    onIntent: (OnboardingIntent) -> Unit,
) {
    ScreenBackground(BackgroundStyle.KITCHEN) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProgressSegments(
                count = state.pages.size,
                current = state.currentPage,
                modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
            )

            Box(
                modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth().height(48.dp).padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                if (!state.isLastPage) {
                    SkipChip(onClick = { onIntent(OnboardingIntent.SkipClicked) })
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) { page ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(state.pages[page].image),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 4.dp, vertical = 8.dp)
                            .graphicsLayer {
                                val pageOffset = ((pagerState.currentPage - page) +
                                        pagerState.currentPageOffsetFraction).absoluteValue
                                val fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                alpha = lerp(0.3f, 1f, fraction)
                                val scale = lerp(0.8f, 1f, fraction)
                                scaleX = scale
                                scaleY = scale
                            },
                    )
                }
            }

            SoftCard(
                modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedContent(
                    targetState = state.currentPage,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                ) { index ->
                    val page = state.pages[index]
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = stringResource(page.title),
                            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 26.sp, lineHeight = 32.sp),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = stringResource(page.description),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 15.sp,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight.Medium,
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AnimatedVisibility(visible = !state.isFirstPage) {
                        BackSquareButton(onClick = { onIntent(OnboardingIntent.BackClicked) })
                    }
                    PrimaryButton(
                        text = stringResource(if (state.isLastPage) Res.string.onboarding_start else Res.string.onboarding_next),
                        onClick = { onIntent(OnboardingIntent.NextClicked) },
                        trailingIcon = Res.drawable.ic_arrow_forward,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressSegments(count: Int, current: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(count) { index ->
            val color by animateColorAsState(
                if (index <= current) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(color),
            )
        }
    }
}

@Composable
private fun SkipChip(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = stringResource(Res.string.onboarding_skip),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun BackSquareButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(58.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_arrow_back),
            contentDescription = stringResource(Res.string.onboarding_back),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(24.dp),
        )
    }
}
