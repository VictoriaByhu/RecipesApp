package com.victoria.recipes.home

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.victoria.recipes.domain.model.ThemeMode
import com.victoria.recipes.locale.AppLanguage
import com.victoria.recipes.mvi.ObserveEffects
import com.victoria.recipes.theme.HeroDarkEnd
import com.victoria.recipes.theme.HeroDarkStart
import com.victoria.recipes.theme.HeroLightEnd
import com.victoria.recipes.theme.HeroLightStart
import com.victoria.recipes.theme.LocalDarkTheme
import com.victoria.recipes.ui.BackgroundStyle
import com.victoria.recipes.ui.IconBox
import com.victoria.recipes.ui.ScreenBackground
import com.victoria.recipes.ui.SegmentedSelector
import com.victoria.recipes.ui.SoftCard
import com.victoria.recipes.ui.cardShadowColor
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import recipesapp.shared.generated.resources.Res
import recipesapp.shared.generated.resources.home_greeting
import recipesapp.shared.generated.resources.home_placeholder_desc
import recipesapp.shared.generated.resources.home_placeholder_title
import recipesapp.shared.generated.resources.home_sign_out
import recipesapp.shared.generated.resources.home_soon
import recipesapp.shared.generated.resources.home_subtitle
import recipesapp.shared.generated.resources.ic_chevron_right
import recipesapp.shared.generated.resources.ic_language
import recipesapp.shared.generated.resources.ic_logout
import recipesapp.shared.generated.resources.ic_palette
import recipesapp.shared.generated.resources.ic_replay
import recipesapp.shared.generated.resources.img_cat
import recipesapp.shared.generated.resources.img_mascot_cook
import recipesapp.shared.generated.resources.settings_language
import recipesapp.shared.generated.resources.settings_language_system
import recipesapp.shared.generated.resources.settings_show_onboarding
import recipesapp.shared.generated.resources.settings_theme
import recipesapp.shared.generated.resources.settings_theme_dark
import recipesapp.shared.generated.resources.settings_theme_light
import recipesapp.shared.generated.resources.settings_theme_system
import recipesapp.shared.generated.resources.settings_title

@Composable
fun HomeScreen(
    onNavigateToOnboarding: () -> Unit,
    onSignedOut: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEffects(viewModel.effects) { effect ->
        when (effect) {
            HomeEffect.NavigateToOnboarding -> onNavigateToOnboarding()
            HomeEffect.NavigateToAuth -> onSignedOut()
        }
    }

    HomeContent(state = state, onIntent = viewModel::onIntent)
}

@Composable
private fun HomeContent(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
) {
    val themeModes = ThemeMode.entries
    val languageTags = listOf<String?>(null) + AppLanguage.entries.map { it.tag }

    ScreenBackground(BackgroundStyle.WAVES) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .widthIn(max = 640.dp)
                .fillMaxWidth()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Header()
            HeroCard()

            Text(
                text = stringResource(Res.string.settings_title),
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 4.dp),
            )

            SoftCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = 18.dp,
                spacing = 18.dp,
            ) {
                SettingBlock(icon = Res.drawable.ic_palette, title = stringResource(Res.string.settings_theme)) {
                    SegmentedSelector(
                        options = listOf(
                            stringResource(Res.string.settings_theme_system),
                            stringResource(Res.string.settings_theme_light),
                            stringResource(Res.string.settings_theme_dark),
                        ),
                        selectedIndex = themeModes.indexOf(state.themeMode),
                        onSelect = { onIntent(HomeIntent.ThemeModeSelected(themeModes[it])) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                SettingBlock(icon = Res.drawable.ic_language, title = stringResource(Res.string.settings_language)) {
                    SegmentedSelector(
                        options = listOf(stringResource(Res.string.settings_language_system)) +
                                AppLanguage.entries.map { it.nativeName },
                        selectedIndex = languageTags.indexOf(state.languageTag).coerceAtLeast(0),
                        onSelect = { onIntent(HomeIntent.LanguageSelected(languageTags[it])) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                ActionRow(
                    icon = Res.drawable.ic_replay,
                    label = stringResource(Res.string.settings_show_onboarding),
                    background = MaterialTheme.colorScheme.background,
                    onClick = { onIntent(HomeIntent.ShowOnboardingClicked) },
                )
            }

            ActionRow(
                icon = Res.drawable.ic_logout,
                label = stringResource(Res.string.home_sign_out),
                background = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.error,
                iconBackground = MaterialTheme.colorScheme.errorContainer,
                showChevron = false,
                elevated = true,
                onClick = { onIntent(HomeIntent.SignOutClicked) },
            )
        }
    }
}

@Composable
private fun Header() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(Res.drawable.img_cat),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(50.dp).padding(top = 3.dp),
            )
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(
                text = stringResource(Res.string.home_greeting),
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 26.sp),
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = stringResource(Res.string.home_subtitle),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun HeroCard() {
    val dark = LocalDarkTheme.current
    val gradient = if (dark) listOf(HeroDarkStart, HeroDarkEnd) else listOf(HeroLightStart, HeroLightEnd)
    val textColor = if (dark) Color(0xFFFFE9D6) else Color(0xFF3B2418)
    val mutedColor = if (dark) Color(0xFFE6C3A8) else Color(0xFF5E3A27)
    val shape = MaterialTheme.shapes.large

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(168.dp)
            .shadow(16.dp, shape, ambientColor = cardShadowColor(), spotColor = cardShadowColor())
            .clip(shape)
            .background(Brush.horizontalGradient(gradient)),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = 36.dp)
                .size(190.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = if (dark) 0.06f else 0.25f)),
        )
        Image(
            painter = painterResource(Res.drawable.img_mascot_cook),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 22.dp, y = 18.dp)
                .height(170.dp),
        )
        Column(
            modifier = Modifier.padding(20.dp).fillMaxWidth(0.58f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = if (dark) 0.12f else 0.55f))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
            ) {
                Text(stringResource(Res.string.home_soon), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textColor)
            }
            Text(
                text = stringResource(Res.string.home_placeholder_title),
                fontSize = 20.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textColor,
            )
            Text(
                text = stringResource(Res.string.home_placeholder_desc),
                fontSize = 12.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight.Medium,
                color = mutedColor,
            )
        }
    }
}

@Composable
private fun SettingBlock(
    icon: DrawableResource,
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        content()
    }
}

@Composable
private fun ActionRow(
    icon: DrawableResource,
    label: String,
    background: Color,
    onClick: () -> Unit,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    iconBackground: Color = MaterialTheme.colorScheme.primaryContainer,
    showChevron: Boolean = true,
    elevated: Boolean = false,
) {
    val shape = MaterialTheme.shapes.small
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (elevated) 56.dp else 52.dp)
            .then(
                if (elevated) Modifier.shadow(8.dp, shape, ambientColor = cardShadowColor(), spotColor = cardShadowColor())
                else Modifier
            )
            .clip(shape)
            .background(background)
            .clickable(onClick = onClick)
            .padding(start = 14.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconBox(
            icon = icon,
            tint = if (contentColor == MaterialTheme.colorScheme.onSurface) MaterialTheme.colorScheme.primary else contentColor,
            background = iconBackground,
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = contentColor,
            modifier = Modifier.weight(1f),
        )
        if (showChevron) {
            Icon(
                painter = painterResource(Res.drawable.ic_chevron_right),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}
