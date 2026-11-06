package com.victoria.recipes.data.repository

import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import com.victoria.recipes.domain.model.ThemeMode
import com.victoria.recipes.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferencesRepositoryImpl(private val settings: Settings) : PreferencesRepository {

    private val log = Logger.withTag("PreferencesRepository")

    private val _themeMode = MutableStateFlow(readThemeMode())
    override val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _languageTag = MutableStateFlow(settings.getStringOrNull(KEY_LANGUAGE))
    override val languageTag: StateFlow<String?> = _languageTag.asStateFlow()

    override fun setThemeMode(mode: ThemeMode) {
        settings[KEY_THEME_MODE] = mode.name
        _themeMode.value = mode
        log.i { "Theme mode changed to $mode" }
    }

    override fun setLanguageTag(tag: String?) {
        if (tag == null) {
            settings.remove(KEY_LANGUAGE)
        } else {
            settings[KEY_LANGUAGE] = tag
        }
        _languageTag.value = tag
        log.i { "Language changed to ${tag ?: "system"}" }
    }

    private fun readThemeMode(): ThemeMode {
        val stored: String = settings[KEY_THEME_MODE, ThemeMode.SYSTEM.name]
        return ThemeMode.entries.firstOrNull { it.name == stored } ?: ThemeMode.SYSTEM
    }

    private companion object {
        const val KEY_THEME_MODE = "theme_mode"
        const val KEY_LANGUAGE = "language"
    }
}
