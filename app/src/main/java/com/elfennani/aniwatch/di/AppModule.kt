package com.elfennani.aniwatch.di

import android.content.Context
import com.elfennani.aniwatch.data_old.local.dao.CachedEpisodesDao
import com.elfennani.aniwatch.data_old.local.dao.CachedListingDao
import com.elfennani.aniwatch.data_old.local.dao.CachedShowDao
import com.elfennani.aniwatch.data_old.local.dao.CachedUserDao
import com.elfennani.aniwatch.data_old.local.dao.DownloadDao
import com.elfennani.aniwatch.data_old.local.dao.SessionDao
import com.elfennani.aniwatch.data_old.remote.APIService
import com.elfennani.aniwatch.data_old.repository.ActivityRepository
import com.elfennani.aniwatch.data_old.repository.DownloadRepository
import com.elfennani.aniwatch.data_old.repository.SessionRepository
import com.elfennani.aniwatch.data_old.repository.ShowRepository
import com.elfennani.aniwatch.data_old.repository.UserRepository
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
        cachedListingDao: CachedListingDao,
        cachedShowDao: CachedShowDao,
        cachedEpisodesDao: CachedEpisodesDao,
    ): ShowRepository =
        ShowRepository(
            apiService,
            cachedListingDao,
            cachedShowDao,
            cachedEpisodesDao,
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
    fun provideUserRepository(
        apiService: APIService,
        cachedUserDao: CachedUserDao,
        @ApplicationContext context: Context
    ) = UserRepository(
        apiService = apiService,
        cachedUserDao = cachedUserDao,
        context = context
    )

    @Provides
    @Singleton
    fun provideDownloadRepository(downloadDao: DownloadDao) = DownloadRepository(downloadDao)
}