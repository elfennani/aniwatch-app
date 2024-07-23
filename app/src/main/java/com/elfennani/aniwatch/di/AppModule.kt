package com.elfennani.aniwatch.di

import com.elfennani.aniwatch.data.local.dao.CachedShowDao
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.data.local.dao.WatchingShowsDao
import com.elfennani.aniwatch.data.remote.APIService
import com.elfennani.aniwatch.data.repository.SessionRepository
import com.elfennani.aniwatch.data.repository.ShowRepository
import dagger.Module

import dagger.Provides
import dagger.hilt.InstallIn
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
        cachedShowDao: CachedShowDao
    ): ShowRepository = ShowRepository(apiService, watchingShowsDao, cachedShowDao)

    @Provides
    @Singleton
    fun provideSessionRepository(sessionDao: SessionDao) =
        SessionRepository(sessionDao)
}