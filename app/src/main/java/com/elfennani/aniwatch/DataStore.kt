package com.elfennani.aniwatch

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

const val SESSION_ID_KEY = "session_id"
val Preferences.sessionId: Long? get()= this[longPreferencesKey(SESSION_ID_KEY)]
var MutablePreferences.sessionId: Long? get() = this[longPreferencesKey(SESSION_ID_KEY)]
    set(value) {
        if (value == null) {
            this.remove(longPreferencesKey(SESSION_ID_KEY))
        } else {
            this[longPreferencesKey(SESSION_ID_KEY)] = value
        }
    }