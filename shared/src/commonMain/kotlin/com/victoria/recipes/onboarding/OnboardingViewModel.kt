package com.victoria.recipes.onboarding

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.victoria.recipes.domain.usecase.CompleteOnboardingUseCase
import com.victoria.recipes.domain.usecase.auth.IsAuthorizedUseCase
import com.victoria.recipes.mvi.MviViewModel
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val completeOnboarding: CompleteOnboardingUseCase,
    private val isAuthorized: IsAuthorizedUseCase,
) : MviViewModel<OnboardingState, OnboardingIntent, OnboardingEffect>(OnboardingState()) {

    private val log = Logger.withTag("Onboarding")

    override fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.PageChanged -> {
                setState { copy(currentPage = intent.page) }
                log.i { "Current page: ${intent.page + 1}/${currentState.pages.size}" }
            }
            OnboardingIntent.NextClicked -> {
                if (currentState.isLastPage) {
                    finish()
                } else {
                    sendEffect(OnboardingEffect.ScrollToPage(currentState.currentPage + 1))
                }
            }
            OnboardingIntent.BackClicked -> {
                if (!currentState.isFirstPage) {
                    sendEffect(OnboardingEffect.ScrollToPage(currentState.currentPage - 1))
                }
            }
            OnboardingIntent.SkipClicked -> {
                log.i { "Onboarding skipped on page ${currentState.currentPage + 1}" }
                finish()
            }
        }
    }

    private fun finish() {
        completeOnboarding()
        viewModelScope.launch {
            sendEffect(if (isAuthorized()) OnboardingEffect.NavigateToHome else OnboardingEffect.NavigateToAuth)
        }
    }
}
