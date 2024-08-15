package com.elfennani.aniwatch.di

import android.content.Context
import com.elfennani.aniwatch.data.local.dao.CachedEpisodesDao
import com.elfennani.aniwatch.data.local.dao.CachedShowDao
import com.elfennani.aniwatch.data.local.dao.CachedUserDao
import com.elfennani.aniwatch.data.local.dao.DownloadDao
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.data.local.dao.WatchingShowsDao
import com.elfennani.aniwatch.data.remote.APIService
import com.elfennani.aniwatch.data.repository.ActivityRepository
import com.elfennani.aniwatch.data.repository.DownloadRepository
import com.elfennani.aniwatch.data.repository.SessionRepository
import com.elfennani.aniwatch.data.repository.ShowRepository
import com.elfennani.aniwatch.data.repository.UserRepository
import dagger.Binds
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
        watchingShowsDao: WatchingShowsDao,
        cachedShowDao: CachedShowDao,
        cachedEpisodesDao: CachedEpisodesDao,
        downloadRepository: DownloadRepository
    ): ShowRepository =
        ShowRepository(
            apiService,
            watchingShowsDao,
            cachedShowDao,
            cachedEpisodesDao,
            downloadRepository
        )

    @Provides
    @Singleton
    fun provideSessionRepository(sessionDao: SessionDao) =
        SessionRepository(sessionDao)

    @Provides
    @Singleton
    fun provideActivityRepository(apiService: APIService) = ActivityRepository(apiService)

    @Provides
    @Singleton
    fun provideDownloadRepository(
        cachedShowDao: CachedShowDao,
        downloadDao: DownloadDao,
        @ApplicationContext context: Context,
    ) = DownloadRepository(
        cachedShowDao = cachedShowDao,
        downloadDao = downloadDao,
        context = context
    )


    @Provides
    @Singleton
    fun provideUserRepository(
        apiService: APIService,
        cachedUserDao: CachedUserDao,
        @ApplicationContext context: Context
    ) = UserRepository(
        apiService = apiService,
        cachedUserDao = cachedUserDao,
        context = context
    )
}