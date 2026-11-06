package com.victoria.recipes.domain.repository

import com.victoria.recipes.domain.model.ThemeMode
import kotlinx.coroutines.flow.StateFlow

interface PreferencesRepository {
    val themeMode: StateFlow<ThemeMode>
    val languageTag: StateFlow<String?>
    fun setThemeMode(mode: ThemeMode)
    fun setLanguageTag(tag: String?)
}
