package com.elfennani.aniwatch.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.elfennani.aniwatch.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
    private val Context.dataStore by preferencesDataStore(name = "settings")

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room
        .databaseBuilder(context, AppDatabase::class.java, "app_dp")
        .build()

    @Provides
    @Singleton
    fun provideSessionDao(database: AppDatabase) = database.sessionDao()

    @Provides
    @Singleton
    fun provideActivityDao(database: AppDatabase) = database.activityDao()

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase) = database.userDao()

    @Provides
    @Singleton
    fun provideShowDao(database: AppDatabase) = database.showDao()

    @Provides
    @Singleton
    fun provideRelationDao(database: AppDatabase) = database.relationDao()

    @Provides
    @Singleton
    fun provideCharacterDao(database: AppDatabase) = database.characterDao()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context) = context.dataStore
}