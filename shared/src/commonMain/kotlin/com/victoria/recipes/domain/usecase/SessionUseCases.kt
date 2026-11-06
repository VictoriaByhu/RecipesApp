package com.victoria.recipes.domain.usecase

import com.victoria.recipes.domain.repository.SessionRepository

class IsSignedInUseCase(private val repository: SessionRepository) {
    operator fun invoke(): Boolean = repository.isSignedIn()
}

class SignInUseCase(private val repository: SessionRepository) {
    operator fun invoke(username: String) = repository.saveSession(username.trim())
}

class SignOutUseCase(private val repository: SessionRepository) {
    operator fun invoke() = repository.clearSession()
}
