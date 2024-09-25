package com.elfennani.aniwatch.domain.repositories

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import com.elfennani.aniwatch.domain.models.Session

interface SessionRepository {
    suspend fun getCurrentSession(): Session?
    suspend fun removeCurrentSession()
    suspend fun addNewSession(accessToken:String, expiration: Long)

    fun initiateAuthFlow()

    companion object{
        val SESSION_KEY = intPreferencesKey("session_id")
    }
}