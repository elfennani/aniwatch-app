package com.elfennani.aniwatch.di

import android.content.Context
import com.elfennani.aniwatch.data.local.dao.EpisodeDao
import com.elfennani.aniwatch.data.local.dao.ListingDao
import com.elfennani.aniwatch.data.local.dao.ShowDao
import com.elfennani.aniwatch.data.local.dao.UserDao
import com.elfennani.aniwatch.data.local.dao.DownloadDao
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.data.remote.APIService
import com.elfennani.aniwatch.data.repository.ActivityRepository
import com.elfennani.aniwatch.data.repository.DownloadRepository
import com.elfennani.aniwatch.data.repository.SessionRepository
import com.elfennani.aniwatch.data.repository.ShowRepository
import com.elfennani.aniwatch.data.repository.UserRepository
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
        listingDao: ListingDao,
        showDao: ShowDao,
        cachedEpisodesDao: EpisodeDao,
    ): ShowRepository =
        ShowRepository(
            apiService,
            listingDao,
            showDao,
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
        userDao: UserDao,
        @ApplicationContext context: Context
    ) = UserRepository(
        apiService = apiService,
        userDao = userDao,
        context = context
    )

    @Provides
    @Singleton
    fun provideDownloadRepository(downloadDao: DownloadDao) = DownloadRepository(downloadDao)
}