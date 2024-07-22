package com.elfennani.aniwatch.presentation.screens.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    fun initiateLoginFlow(context: Context) {
        val baseUrl = "https://anilist.co/api/v2/oauth/authorize"
        val url = baseUrl.toHttpUrl().newBuilder()
            .addQueryParameter("client_id","17834")
            .addQueryParameter("response_type", "token")
            .build()
            .toString()

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

}