package com.elfennani.aniwatch.ui.screens.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.elfennani.aniwatch.domain.repositories.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    fun initiateLoginFlow(context: Context) {
        sessionRepository.initiateAuthFlow()
    }

}