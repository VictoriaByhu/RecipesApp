package com.victoria.recipes.domain.repository

interface SessionRepository {
    fun isSignedIn(): Boolean
    fun currentUsername(): String?
    fun saveSession(username: String)
    fun clearSession()
}
