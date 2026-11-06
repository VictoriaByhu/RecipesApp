package com.victoria.recipes.domain.usecase

import com.victoria.recipes.domain.model.UsernameError

class ValidateUsernameUseCase {

    operator fun invoke(username: String): UsernameError? {
        val value = username.trim()
        return when {
            value.isEmpty() -> UsernameError.EMPTY
            value.length < MIN_LENGTH -> UsernameError.TOO_SHORT
            value.length > MAX_LENGTH -> UsernameError.TOO_LONG
            !ALLOWED.matches(value) -> UsernameError.INVALID_CHARACTERS
            else -> null
        }
    }

    companion object {
        const val MIN_LENGTH = 3
        const val MAX_LENGTH = 30
        private val ALLOWED = Regex("^[A-Za-z0-9._-]+$")
    }
}
