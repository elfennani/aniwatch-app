package com.elfennani.aniwatch.di

import android.content.Context
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.data.remote.APIService
import com.elfennani.aniwatch.data.remote.AuthInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {
    @Provides
    fun provideBaseUrl(): String = "https://aniwatch-api-green.vercel.app"

    @Provides
    @Singleton
    fun provideAuthInterceptor(sessionDao: SessionDao, @ApplicationContext context: Context):AuthInterceptor{
        return AuthInterceptor(sessionDao,context)
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, authInterceptor: AuthInterceptor): Retrofit {
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(interceptor)
            .build()

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): APIService = retrofit.create(APIService::class.java)
}