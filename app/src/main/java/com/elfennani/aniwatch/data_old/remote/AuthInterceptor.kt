package com.elfennani.aniwatch.data_old.remote

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.elfennani.aniwatch.data_old.local.dao.SessionDao
import com.elfennani.aniwatch.dataStore
import com.elfennani.aniwatch.sessionId
import com.elfennani.aniwatch.sessionIdKey
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
                    val token = runBlocking { sessionDao.getSessionById(sessionId)?.token }
                    if(token.isNullOrEmpty()){
                        runBlocking {
                            context.dataStore.edit {
                                it.remove(it.sessionIdKey)
                            }
                        }
                    }

                    addHeader("Authorization", "Bearer $token")
                }
            }
            .build()

        return chain.proceed(newRequest)
    }
}