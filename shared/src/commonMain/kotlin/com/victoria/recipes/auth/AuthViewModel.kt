package com.victoria.recipes.auth

import co.touchlab.kermit.Logger
import com.victoria.recipes.domain.usecase.SignInUseCase
import com.victoria.recipes.domain.usecase.ValidatePasswordUseCase
import com.victoria.recipes.domain.usecase.ValidateUsernameUseCase
import com.victoria.recipes.mvi.MviViewModel

class AuthViewModel(
    private val validateUsername: ValidateUsernameUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val signInUser: SignInUseCase,
) : MviViewModel<AuthState, AuthIntent, AuthEffect>(AuthState()) {

    private val log = Logger.withTag("Auth")

    override fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.UsernameChanged -> setState {
                copy(
                    username = intent.value,
                    usernameError = if (isValidationActive) validateUsername(intent.value) else null,
                )
            }
            is AuthIntent.PasswordChanged -> setState {
                copy(
                    password = intent.value,
                    passwordError = if (isValidationActive) validatePassword(intent.value) else null,
                )
            }
            AuthIntent.TogglePasswordVisibility -> setState {
                copy(isPasswordVisible = !isPasswordVisible)
            }
            AuthIntent.SignInClicked -> signIn()
        }
    }

    private fun signIn() {
        val usernameError = validateUsername(currentState.username)
        val passwordError = validatePassword(currentState.password)
        setState {
            copy(
                usernameError = usernameError,
                passwordError = passwordError,
                isValidationActive = true,
            )
        }
        if (usernameError == null && passwordError == null) {
            log.i { "Sign in: ${currentState.username.trim()}" }
            signInUser(currentState.username)
            sendEffect(AuthEffect.NavigateToHome)
        } else {
            log.w { "Validation failed: username=$usernameError, password=$passwordError" }
        }
    }
}
