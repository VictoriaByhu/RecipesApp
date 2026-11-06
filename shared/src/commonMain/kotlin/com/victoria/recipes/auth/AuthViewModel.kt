package com.victoria.recipes.auth

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.victoria.recipes.domain.model.onFailure
import com.victoria.recipes.domain.model.onSuccess
import com.victoria.recipes.domain.usecase.ValidatePasswordUseCase
import com.victoria.recipes.domain.usecase.ValidateUsernameUseCase
import com.victoria.recipes.domain.usecase.auth.LoginUseCase
import com.victoria.recipes.mvi.MviViewModel
import kotlinx.coroutines.launch

class AuthViewModel(
    private val validateUsername: ValidateUsernameUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val login: LoginUseCase,
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
        if (currentState.isLoading) return
        val usernameError = validateUsername(currentState.username)
        val passwordError = validatePassword(currentState.password)
        setState {
            copy(
                usernameError = usernameError,
                passwordError = passwordError,
                isValidationActive = true,
            )
        }
        if (usernameError != null || passwordError != null) {
            log.w { "Validation failed: username=$usernameError, password=$passwordError" }
            return
        }

        setState { copy(isLoading = true) }
        viewModelScope.launch {
            login(currentState.username, currentState.password)
                .onSuccess { user ->
                    log.i { "Signed in as ${user.username}" }
                    sendEffect(AuthEffect.NavigateToHome)
                }
                .onFailure { error ->
                    log.w { "Sign in failed: $error" }
                    sendEffect(AuthEffect.ShowErrorSnackbar(error))
                }
            setState { copy(isLoading = false) }
        }
    }
}
