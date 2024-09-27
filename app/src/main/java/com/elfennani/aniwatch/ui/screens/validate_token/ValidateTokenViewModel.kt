package com.elfennani.aniwatch.ui.screens.validate_token

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.domain.errors.AppError.Companion.readable
import com.elfennani.aniwatch.domain.models.Result
import com.elfennani.aniwatch.domain.usecases.ValidateTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject

@HiltViewModel
class ValidateTokenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val validateTokenUseCase: ValidateTokenUseCase,
) : ViewModel() {
    private val params = savedStateHandle.get<String>("params")
    private val _errors = MutableStateFlow<List<Int>>(emptyList())
    val errors = _errors.asStateFlow()

    fun validate(context: Context, onSuccess: () -> Unit = {}) {
        val fakeUrl = "https://example.com/?$params"
        val httpUrl = fakeUrl.toHttpUrl()

        val accessToken = httpUrl.queryParameter("access_token")
        val expiration = (httpUrl.queryParameter("expires_in"))

        if (accessToken.isNullOrEmpty() || expiration.isNullOrEmpty()) {
            _errors.update { it + R.string.invalid_token }
            return
        }


        viewModelScope.launch {
            when (val result = validateTokenUseCase(accessToken, expiration.toLong())) {
                is Result.Ok -> {
                    Toast.makeText(context, "Successfully signed in", Toast.LENGTH_SHORT).show()
                    onSuccess()
                }

                is Result.Err -> {
                    _errors.update {
                        it + result.error.readable()
                    }
                }
            }
        }
    }

}