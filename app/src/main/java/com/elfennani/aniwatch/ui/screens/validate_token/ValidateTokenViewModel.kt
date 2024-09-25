package com.elfennani.aniwatch.ui.screens.validate_token

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.data_old.repository.SessionRepository
import com.elfennani.aniwatch.dataStore
import com.elfennani.aniwatch.domain.models.Resource
import com.elfennani.aniwatch.sessionId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class ValidateTokenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sessionRepository: SessionRepository
) : ViewModel() {
    private val params = savedStateHandle.get<String>("params")
    private val _errors = MutableStateFlow<List<Int>>(emptyList());
    val errors = _errors.asStateFlow()

    fun validate(context: Context, onSuccess: () -> Unit = {}){
        val fakeUrl = "https://example.com/?$params"
        val httpUrl = fakeUrl.toHttpUrl()

        val accessToken = httpUrl.queryParameter("access_token")
        val expiration = (httpUrl.queryParameter("expires_in"))

        if(accessToken.isNullOrEmpty() || expiration.isNullOrEmpty()){
            _errors.update { it + R.string.invalid_token }
            return;
        }

        val expires = Instant.now().epochSecond + expiration.toLong()

        viewModelScope.launch {
            when(val result = sessionRepository.saveSession(accessToken,expires)){
                is Resource.Success -> {
                    val id = result.data!!
                    context.dataStore.edit {
                        it.sessionId = id
                    }

                    onSuccess()
                }
                is Resource.Error -> {
                    _errors.update { it + result.message!! }
                }
            }
        }
    }

}