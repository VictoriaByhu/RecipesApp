package com.victoria.recipes.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

enum class ThemeMode { SYSTEM, LIGHT, DARK }

class AppSettings(private val settings: Settings = Settings()) {

    private val log = Logger.withTag("AppSettings")

    var onboardingCompleted by mutableStateOf(settings[KEY_ONBOARDING_COMPLETED, false])
        private set

    var themeMode by mutableStateOf(readThemeMode())
        private set


    var languageTag by mutableStateOf(settings.getStringOrNull(KEY_LANGUAGE))
        private set

    fun completeOnboarding() {
        settings[KEY_ONBOARDING_COMPLETED] = true
        onboardingCompleted = true
        log.i { "Onboarding completed" }
    }

    fun resetOnboarding() {
        settings[KEY_ONBOARDING_COMPLETED] = false
        onboardingCompleted = false
        log.i { "Onboarding reset" }
    }

    fun updateThemeMode(mode: ThemeMode) {
        settings[KEY_THEME_MODE] = mode.name
        themeMode = mode
        log.i { "Theme mode changed to $mode" }
    }

    fun updateLanguage(tag: String?) {
        if (tag == null) {
            settings.remove(KEY_LANGUAGE)
        } else {
            settings[KEY_LANGUAGE] = tag
        }
        languageTag = tag
        log.i { "Language changed to ${tag ?: "system"}" }
    }

    private fun readThemeMode(): ThemeMode {
        val stored: String = settings[KEY_THEME_MODE, ThemeMode.SYSTEM.name]
        return ThemeMode.entries.firstOrNull { it.name == stored } ?: ThemeMode.SYSTEM
    }

    private companion object {
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        const val KEY_THEME_MODE = "theme_mode"
        const val KEY_LANGUAGE = "language"
    }
}
