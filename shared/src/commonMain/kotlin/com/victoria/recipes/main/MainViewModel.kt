package com.victoria.recipes.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.victoria.recipes.domain.model.ThemeMode
import com.victoria.recipes.domain.usecase.IsOnboardingCompletedUseCase
import com.victoria.recipes.domain.usecase.ObserveLanguageUseCase
import com.victoria.recipes.domain.usecase.ObserveThemeModeUseCase
import com.victoria.recipes.domain.usecase.auth.IsAuthorizedUseCase
import com.victoria.recipes.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MainState(
    val themeMode: ThemeMode,
    val languageTag: String?,
    val startRoute: Route? = null,
)

class MainViewModel(
    isOnboardingCompleted: IsOnboardingCompletedUseCase,
    isAuthorized: IsAuthorizedUseCase,
    observeThemeMode: ObserveThemeModeUseCase,
    observeLanguage: ObserveLanguageUseCase,
) : ViewModel() {

    private val startRoute = MutableStateFlow<Route?>(null)

    val state: StateFlow<MainState> = combine(
        observeThemeMode(),
        observeLanguage(),
        startRoute,
    ) { theme, language, route ->
        MainState(themeMode = theme, languageTag = language, startRoute = route)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MainState(observeThemeMode().value, observeLanguage().value),
    )

    init {
        viewModelScope.launch {
            val route = when {
                !isOnboardingCompleted() -> Route.Onboarding
                isAuthorized() -> Route.Home
                else -> Route.Auth
            }
            Logger.withTag("MainViewModel").i { "Start destination: $route" }
            startRoute.value = route
        }
    }
}
