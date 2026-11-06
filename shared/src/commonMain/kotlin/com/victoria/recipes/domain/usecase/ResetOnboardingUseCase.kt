package com.victoria.recipes.domain.usecase

import com.victoria.recipes.domain.repository.OnboardingRepository

class ResetOnboardingUseCase(private val repository: OnboardingRepository) {
    operator fun invoke() = repository.setOnboardingCompleted(false)
}
