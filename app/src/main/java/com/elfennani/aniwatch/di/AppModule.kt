package com.elfennani.aniwatch.di

import android.content.Context
import androidx.room.Room
import com.elfennani.aniwatch.data.local.Database
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.data.local.dao.WatchingShowsDao
import com.elfennani.aniwatch.data.remote.APIService
import com.elfennani.aniwatch.data.repository.ShowRepositoryImpl
import com.elfennani.aniwatch.data.usecases.SaveUserSessionUseCaseImpl
import com.elfennani.aniwatch.domain.repository.ShowRepository
import com.elfennani.aniwatch.domain.usecases.GetShowByIdUseCase
import com.elfennani.aniwatch.domain.usecases.GetShowsByStatusUseCase
import com.elfennani.aniwatch.domain.usecases.SaveUserSessionUseCase
import dagger.Module

import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideShowRepository(
        apiService: APIService,
        watchingShowsDao: WatchingShowsDao
    ): ShowRepository =
        ShowRepositoryImpl(apiService, watchingShowsDao)

    @Provides
    @Singleton
    fun provideShowByIdUseCase(showRepository: ShowRepository) = GetShowByIdUseCase(showRepository)

    @Provides
    @Singleton
    fun provideShowsByStatusUseCase(showRepository: ShowRepository) =
        GetShowsByStatusUseCase(showRepository)

    @Provides
    @Singleton
    fun provideSaveUserSessionUseCase(sessionDao: SessionDao): SaveUserSessionUseCase =
        SaveUserSessionUseCaseImpl(sessionDao)
}