package com.victoria.recipes.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import recipesapp.shared.generated.resources.Res
import recipesapp.shared.generated.resources.ic_favorite
import recipesapp.shared.generated.resources.ic_menu_book
import recipesapp.shared.generated.resources.ic_restaurant_menu
import recipesapp.shared.generated.resources.ic_skillet
import recipesapp.shared.generated.resources.onboarding_back
import recipesapp.shared.generated.resources.onboarding_desc_1
import recipesapp.shared.generated.resources.onboarding_desc_2
import recipesapp.shared.generated.resources.onboarding_desc_3
import recipesapp.shared.generated.resources.onboarding_desc_4
import recipesapp.shared.generated.resources.onboarding_next
import recipesapp.shared.generated.resources.onboarding_page_counter
import recipesapp.shared.generated.resources.onboarding_skip
import recipesapp.shared.generated.resources.onboarding_start
import recipesapp.shared.generated.resources.onboarding_title_1
import recipesapp.shared.generated.resources.onboarding_title_2
import recipesapp.shared.generated.resources.onboarding_title_3
import recipesapp.shared.generated.resources.onboarding_title_4
import kotlin.math.absoluteValue

private data class OnboardingPage(
    val icon: DrawableResource,
    val title: StringResource,
    val description: StringResource,
)

private val pages = listOf(
    OnboardingPage(Res.drawable.ic_restaurant_menu, Res.string.onboarding_title_1, Res.string.onboarding_desc_1),
    OnboardingPage(Res.drawable.ic_menu_book, Res.string.onboarding_title_2, Res.string.onboarding_desc_2),
    OnboardingPage(Res.drawable.ic_favorite, Res.string.onboarding_title_3, Res.string.onboarding_desc_3),
    OnboardingPage(Res.drawable.ic_skillet, Res.string.onboarding_title_4, Res.string.onboarding_desc_4),
)

private val log = Logger.withTag("Onboarding")


@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == pages.lastIndex

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            log.i { "Current page: ${page + 1}/${pages.size}" }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(Res.string.onboarding_page_counter, pagerState.currentPage + 1, pages.size),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            TextButton(
                onClick = {
                    log.i { "Onboarding skipped on page ${pagerState.currentPage + 1}" }
                    onFinished()
                },
                enabled = !isLastPage,
            ) {
                Text(stringResource(Res.string.onboarding_skip))
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) { page ->
            OnboardingPageContent(
                page = pages[page],
                modifier = Modifier.graphicsLayer {

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

        PagerIndicator(pagerState = pagerState, modifier = Modifier.padding(vertical = 24.dp))


        Row(
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } },
                enabled = pagerState.currentPage > 0,
                modifier = Modifier.weight(1f).height(52.dp),
            ) {
                Text(stringResource(Res.string.onboarding_back))
            }
            Button(
                onClick = {
                    if (isLastPage) {
                        onFinished()
                    } else {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    }
                },
                modifier = Modifier.weight(1f).height(52.dp),
            ) {
                Text(
                    stringResource(if (isLastPage) Res.string.onboarding_start else Res.string.onboarding_next),
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(page.icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(80.dp),
                )
            }
        }
        Spacer(Modifier.height(40.dp))
        Text(
            text = stringResource(page.title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(page.description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 420.dp),
        )
    }
}

@Composable
private fun PagerIndicator(pagerState: PagerState, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pagerState.pageCount) { index ->
            val selected = pagerState.currentPage == index
            val width by animateDpAsState(if (selected) 28.dp else 10.dp)
            val color by animateColorAsState(
                if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            )
            Box(
                modifier = Modifier
                    .height(10.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(color),
            )
        }
    }
}
