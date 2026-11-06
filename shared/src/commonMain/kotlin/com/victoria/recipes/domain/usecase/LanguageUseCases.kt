package com.victoria.recipes.domain.usecase

import com.victoria.recipes.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.StateFlow

class ObserveLanguageUseCase(private val repository: PreferencesRepository) {
    operator fun invoke(): StateFlow<String?> = repository.languageTag
}

class SetLanguageUseCase(private val repository: PreferencesRepository) {
    operator fun invoke(tag: String?) = repository.setLanguageTag(tag)
}
