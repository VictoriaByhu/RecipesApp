package com.victoria.recipes.home

import com.victoria.recipes.domain.model.ThemeMode

data class HomeState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val languageTag: String? = null,
)

sealed interface HomeIntent {
    data class ThemeModeSelected(val mode: ThemeMode) : HomeIntent
    data class LanguageSelected(val tag: String?) : HomeIntent
    data object ShowOnboardingClicked : HomeIntent
    data object SignOutClicked : HomeIntent
}

sealed interface HomeEffect {
    data object NavigateToOnboarding : HomeEffect
    data object NavigateToAuth : HomeEffect
}
