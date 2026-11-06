package com.victoria.recipes.domain.usecase

import com.victoria.recipes.domain.repository.OnboardingRepository

class IsOnboardingCompletedUseCase(private val repository: OnboardingRepository) {
    operator fun invoke(): Boolean = repository.isOnboardingCompleted()
}
