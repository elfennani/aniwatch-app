package com.elfennani.aniwatch.data.remote

import android.content.Context
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.dataStore
import com.elfennani.aniwatch.sessionId
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor constructor(
    private val sessionDao: SessionDao,
    private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val sessionId = runBlocking { context.dataStore.data.first().sessionId }

        val newRequest = originalRequest.newBuilder()
            .apply {
                if (sessionId != null) {
                    val token = runBlocking { sessionDao.getSessionById(sessionId).token }
                    addHeader("Authorization", "Bearer $token")
                }
            }
            .build()

        return chain.proceed(newRequest)
    }
}