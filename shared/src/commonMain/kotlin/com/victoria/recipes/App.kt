package com.victoria.recipes

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import co.touchlab.kermit.Logger
import com.victoria.recipes.home.HomeScreen
import com.victoria.recipes.locale.AppLocaleEnvironment
import com.victoria.recipes.onboarding.OnboardingScreen
import com.victoria.recipes.settings.AppSettings
import com.victoria.recipes.theme.RecipesTheme

@Composable
@Preview
fun App() {
    val settings = remember { AppSettings() }

    LaunchedEffect(Unit) {
        Logger.setTag("RecipesApp")
        Logger.i { "App started on ${getPlatform().name}" }
    }

    AppLocaleEnvironment(languageTag = settings.languageTag) {
        RecipesTheme(themeMode = settings.themeMode) {
            Crossfade(targetState = settings.onboardingCompleted) { completed ->
                if (completed) {
                    HomeScreen(
                        themeMode = settings.themeMode,
                        onThemeModeChange = settings::updateThemeMode,
                        languageTag = settings.languageTag,
                        onLanguageChange = settings::updateLanguage,
                        onShowOnboarding = settings::resetOnboarding,
                    )
                } else {
                    OnboardingScreen(onFinished = settings::completeOnboarding)
                }
            }
        }
    }
}
