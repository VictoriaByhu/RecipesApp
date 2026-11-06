package com.victoria.recipes.data.repository

import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import com.victoria.recipes.domain.repository.SessionRepository

class SessionRepositoryImpl(private val settings: Settings) : SessionRepository {

    private val log = Logger.withTag("SessionRepository")

    override fun isSignedIn(): Boolean = currentUsername() != null

    override fun currentUsername(): String? = settings.getStringOrNull(KEY_USERNAME)

    override fun saveSession(username: String) {
        settings[KEY_USERNAME] = username
        log.i { "Session saved for $username" }
    }

    override fun clearSession() {
        settings.remove(KEY_USERNAME)
        log.i { "Session cleared" }
    }

    private companion object {
        const val KEY_USERNAME = "session_username"
    }
}
