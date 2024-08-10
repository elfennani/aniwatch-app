package com.elfennani.aniwatch.di

import android.content.Context
import androidx.room.Room
import com.elfennani.aniwatch.data.local.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return Room
            .databaseBuilder(context, Database::class.java, "db")
            .build()
    }

    @Provides
    @Singleton
    fun provideSessionDao(database: Database) = database.sessionDao()

    @Provides
    @Singleton
    fun provideWatchingShowsDao(database: Database) = database.watchingShowsDao()

    @Provides
    @Singleton
    fun provideCachedShowDao(database: Database) = database.cachedShowDao()

    @Provides
    @Singleton
    fun provideCachedEpisodesDao(database: Database) = database.cachedEpisodesDao()

    @Provides
    @Singleton
    fun provideFeedDao(database: Database) = database.feedDao()

    @Provides
    @Singleton
    fun provideDownloadDao(database: Database) = database.downloadDao()
}