package com.elfennani.aniwatch.di

import com.elfennani.aniwatch.data.repository.FeedRepositoryImpl
import com.elfennani.aniwatch.data.repository.SessionRepositoryImpl
import com.elfennani.aniwatch.domain.repositories.FeedRepository
import com.elfennani.aniwatch.domain.repositories.SessionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindSessionRepository(
        sessionRepositoryImpl: SessionRepositoryImpl,
    ): SessionRepository

    @Binds
    abstract fun bindFeedRepository(
        feedRepositoryImpl: FeedRepositoryImpl
    ): FeedRepository
}