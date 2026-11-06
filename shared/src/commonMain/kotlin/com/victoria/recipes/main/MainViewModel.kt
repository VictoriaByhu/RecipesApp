package com.victoria.recipes.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.victoria.recipes.domain.model.ThemeMode
import com.victoria.recipes.domain.usecase.IsOnboardingCompletedUseCase
import com.victoria.recipes.domain.usecase.IsSignedInUseCase
import com.victoria.recipes.domain.usecase.ObserveLanguageUseCase
import com.victoria.recipes.domain.usecase.ObserveThemeModeUseCase
import com.victoria.recipes.navigation.Route
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class MainState(
    val themeMode: ThemeMode,
    val languageTag: String?,
)

class MainViewModel(
    isOnboardingCompleted: IsOnboardingCompletedUseCase,
    isSignedIn: IsSignedInUseCase,
    observeThemeMode: ObserveThemeModeUseCase,
    observeLanguage: ObserveLanguageUseCase,
) : ViewModel() {

    val startRoute: Route = when {
        !isOnboardingCompleted() -> Route.Onboarding
        isSignedIn() -> Route.Home
        else -> Route.Auth
    }

    val state: StateFlow<MainState> = combine(observeThemeMode(), observeLanguage()) { theme, language ->
        MainState(themeMode = theme, languageTag = language)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MainState(observeThemeMode().value, observeLanguage().value),
    )

    init {
        Logger.withTag("MainViewModel").i { "Start destination: $startRoute" }
    }
}
