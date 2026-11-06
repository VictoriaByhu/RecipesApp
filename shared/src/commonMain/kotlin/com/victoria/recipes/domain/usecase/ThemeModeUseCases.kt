package com.victoria.recipes.domain.usecase

import com.victoria.recipes.domain.model.ThemeMode
import com.victoria.recipes.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.StateFlow

class ObserveThemeModeUseCase(private val repository: PreferencesRepository) {
    operator fun invoke(): StateFlow<ThemeMode> = repository.themeMode
}

class SetThemeModeUseCase(private val repository: PreferencesRepository) {
    operator fun invoke(mode: ThemeMode) = repository.setThemeMode(mode)
}
