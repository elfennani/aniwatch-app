package com.elfennani.aniwatch.data.remote

import com.elfennani.aniwatch.domain.repositories.SessionRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionRepository: SessionRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest = originalRequest.newBuilder()
            .apply {
                val session = runBlocking { sessionRepository.getCurrentSession() }
                if (session?.token.isNullOrEmpty()) {
                    runBlocking {
                        sessionRepository.removeCurrentSession()
                    }
                }

                addHeader("Authorization", "Bearer ${session?.token}")
            }
            .build()

        return chain.proceed(newRequest)
    }
}