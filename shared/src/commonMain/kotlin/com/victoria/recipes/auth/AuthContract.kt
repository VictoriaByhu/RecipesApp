package com.victoria.recipes.auth

import com.victoria.recipes.domain.model.PasswordError
import com.victoria.recipes.domain.model.UsernameError

data class AuthState(
    val username: String = "",
    val password: String = "",
    val usernameError: UsernameError? = null,
    val passwordError: PasswordError? = null,
    val isPasswordVisible: Boolean = false,
    val isValidationActive: Boolean = false,
)

sealed interface AuthIntent {
    data class UsernameChanged(val value: String) : AuthIntent
    data class PasswordChanged(val value: String) : AuthIntent
    data object TogglePasswordVisibility : AuthIntent
    data object SignInClicked : AuthIntent
}

sealed interface AuthEffect {
    data object NavigateToHome : AuthEffect
}
