package com.elfennani.aniwatch.di

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.data_old.local.dao.SessionDao
import com.elfennani.aniwatch.data.remote.AuthInterceptor
import com.elfennani.aniwatch.domain.repositories.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AniListApolloClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AllAnimeApolloClient

@Module
@InstallIn(SingletonComponent::class)
class ApolloModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        sessionRepository: SessionRepository
    ): AuthInterceptor {
        return AuthInterceptor(sessionRepository)
    }

    @Provides
    @Singleton
    @AllAnimeApolloClient
    fun provideAllAnimeApolloClient(@ApplicationContext context: Context): ApolloClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val apolloClient = ApolloClient.Builder()
            .serverUrl(context.getString(R.string.allanime_graphql))
            .okHttpClient(client)
            .addHttpHeader("Referer", "https://allanime.to")
            .addHttpHeader(
                "Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/121.0"
            )
            .build()

        return apolloClient
    }

    @Provides
    @Singleton
    @AniListApolloClient
    fun provideAniListApolloClient(
        authInterceptor: AuthInterceptor,
        @ApplicationContext context: Context,
    ): ApolloClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        val apolloClient = ApolloClient.Builder()
            .serverUrl(context.getString(R.string.anilist_graphql))
            .okHttpClient(client)
            .build()

        return apolloClient
    }

}