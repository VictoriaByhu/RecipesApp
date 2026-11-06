package com.victoria.recipes.home

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.victoria.recipes.domain.model.AppError
import com.victoria.recipes.domain.model.onFailure
import com.victoria.recipes.domain.model.onSuccess
import com.victoria.recipes.domain.usecase.ObserveLanguageUseCase
import com.victoria.recipes.domain.usecase.ObserveThemeModeUseCase
import com.victoria.recipes.domain.usecase.ResetOnboardingUseCase
import com.victoria.recipes.domain.usecase.SetLanguageUseCase
import com.victoria.recipes.domain.usecase.SetThemeModeUseCase
import com.victoria.recipes.domain.usecase.auth.GetCurrentUserUseCase
import com.victoria.recipes.domain.usecase.auth.LogoutUseCase
import com.victoria.recipes.mvi.MviViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(
    observeThemeMode: ObserveThemeModeUseCase,
    observeLanguage: ObserveLanguageUseCase,
    private val setThemeMode: SetThemeModeUseCase,
    private val setLanguage: SetLanguageUseCase,
    private val resetOnboarding: ResetOnboardingUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val logout: LogoutUseCase,
) : MviViewModel<HomeState, HomeIntent, HomeEffect>(
    HomeState(themeMode = observeThemeMode().value, languageTag = observeLanguage().value)
) {

    private val log = Logger.withTag("Home")

    init {
        viewModelScope.launch {
            combine(observeThemeMode(), observeLanguage()) { theme, language -> theme to language }
                .collect { (theme, language) ->
                    setState { copy(themeMode = theme, languageTag = language) }
                }
        }
        loadUser()
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ThemeModeSelected -> setThemeMode(intent.mode)
            is HomeIntent.LanguageSelected -> setLanguage(intent.tag)
            HomeIntent.ShowOnboardingClicked -> {
                resetOnboarding()
                sendEffect(HomeEffect.NavigateToOnboarding)
            }
            HomeIntent.SignOutClicked -> signOut()
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            getCurrentUser()
                .onSuccess { user ->
                    log.i { "Current user: ${user.username}" }
                    setState { copy(firstName = user.firstName) }
                }
                .onFailure { error ->
                    log.w { "Failed to load user: $error" }
                    if (error == AppError.Unauthorized) {
                        signOut()
                    } else {
                        sendEffect(HomeEffect.ShowErrorSnackbar(error))
                    }
                }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            logout()
            sendEffect(HomeEffect.NavigateToAuth)
        }
    }
}
