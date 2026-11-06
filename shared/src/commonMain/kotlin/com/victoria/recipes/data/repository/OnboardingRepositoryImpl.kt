package com.victoria.recipes.data.repository

import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import com.victoria.recipes.domain.repository.OnboardingRepository

class OnboardingRepositoryImpl(private val settings: Settings) : OnboardingRepository {

    private val log = Logger.withTag("OnboardingRepository")

    override fun isOnboardingCompleted(): Boolean = settings[KEY_ONBOARDING_COMPLETED, false]

    override fun setOnboardingCompleted(completed: Boolean) {
        settings[KEY_ONBOARDING_COMPLETED] = completed
        log.i { "Onboarding completed = $completed" }
    }

    private companion object {
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
}
