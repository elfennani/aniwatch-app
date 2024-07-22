package com.elfennani.aniwatch.presentation.screens.validate_token

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.dataStore
import com.elfennani.aniwatch.domain.Resource
import com.elfennani.aniwatch.domain.usecases.SaveUserSessionUseCase
import com.elfennani.aniwatch.sessionId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.time.Instant
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ValidateTokenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveUserSessionUseCase: SaveUserSessionUseCase
) : ViewModel() {
    private val params = savedStateHandle.get<String>("params")
    private val _error = MutableStateFlow<String?>(null);
    val error: StateFlow<String?> get() = _error

    fun validate(context: Context, onSuccess: () -> Unit = {}){
        val fakeUrl = "https://example.com/?$params"
        val httpUrl = fakeUrl.toHttpUrl()

        val accessToken = httpUrl.queryParameter("access_token")
        val expiration = (httpUrl.queryParameter("expires_in"))

        if(accessToken.isNullOrEmpty() || expiration.isNullOrEmpty()){
            _error.value = "Invalid Access Token Or Expiration date"
            return;
        }

        val expires = Instant.now().epochSecond + expiration.toLong()

        viewModelScope.launch {
            when(val result = saveUserSessionUseCase(accessToken,expires)){
                is Resource.Success -> {
                    val id = result.data!!
                    context.dataStore.edit {
                        it.sessionId = id
                    }

                    onSuccess()
                }
                is Resource.Error -> {
                    _error.value = result.message
                }
            }
        }
    }

}