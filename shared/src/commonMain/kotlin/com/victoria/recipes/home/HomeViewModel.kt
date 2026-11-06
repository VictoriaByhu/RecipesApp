package com.victoria.recipes.home

import androidx.lifecycle.viewModelScope
import com.victoria.recipes.domain.usecase.ObserveLanguageUseCase
import com.victoria.recipes.domain.usecase.ObserveThemeModeUseCase
import com.victoria.recipes.domain.usecase.ResetOnboardingUseCase
import com.victoria.recipes.domain.usecase.SetLanguageUseCase
import com.victoria.recipes.domain.usecase.SetThemeModeUseCase
import com.victoria.recipes.domain.usecase.SignOutUseCase
import com.victoria.recipes.mvi.MviViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(
    observeThemeMode: ObserveThemeModeUseCase,
    observeLanguage: ObserveLanguageUseCase,
    private val setThemeMode: SetThemeModeUseCase,
    private val setLanguage: SetLanguageUseCase,
    private val resetOnboarding: ResetOnboardingUseCase,
    private val signOut: SignOutUseCase,
) : MviViewModel<HomeState, HomeIntent, HomeEffect>(
    HomeState(themeMode = observeThemeMode().value, languageTag = observeLanguage().value)
) {

    init {
        viewModelScope.launch {
            combine(observeThemeMode(), observeLanguage()) { theme, language -> theme to language }
                .collect { (theme, language) ->
                    setState { copy(themeMode = theme, languageTag = language) }
                }
        }
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ThemeModeSelected -> setThemeMode(intent.mode)
            is HomeIntent.LanguageSelected -> setLanguage(intent.tag)
            HomeIntent.ShowOnboardingClicked -> {
                resetOnboarding()
                sendEffect(HomeEffect.NavigateToOnboarding)
            }
            HomeIntent.SignOutClicked -> {
                signOut()
                sendEffect(HomeEffect.NavigateToAuth)
            }
        }
    }
}
