package com.elfennani.aniwatch.di

import com.elfennani.aniwatch.data.repository.DownloadRepositoryImpl
import com.elfennani.aniwatch.data.repository.FeedRepositoryImpl
import com.elfennani.aniwatch.data.repository.ListingRepositoryImpl
import com.elfennani.aniwatch.data.repository.SessionRepositoryImpl
import com.elfennani.aniwatch.data.repository.ShowRepositoryImpl
import com.elfennani.aniwatch.data.repository.UserRepositoryImpl
import com.elfennani.aniwatch.domain.repositories.DownloadRepository
import com.elfennani.aniwatch.domain.repositories.FeedRepository
import com.elfennani.aniwatch.domain.repositories.ListingRepository
import com.elfennani.aniwatch.domain.repositories.SessionRepository
import com.elfennani.aniwatch.domain.repositories.ShowRepository
import com.elfennani.aniwatch.domain.repositories.UserRepository
import com.elfennani.aniwatch.domain.usecases.FetchListingUseCase
import com.elfennani.aniwatch.domain.usecases.FetchRelationsUseCase
import com.elfennani.aniwatch.domain.usecases.FetchShowUseCase
import com.elfennani.aniwatch.domain.usecases.FetchUserByIdUseCase
import com.elfennani.aniwatch.domain.usecases.FetchViewerUseCase
import com.elfennani.aniwatch.domain.usecases.IncrementEpisodeUseCase
import com.elfennani.aniwatch.domain.usecases.UpdateShowStatusUseCase
import com.elfennani.aniwatch.domain.usecases.ValidateTokenUseCase
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

    @Binds
    abstract fun bindListingRepository(
        listingRepositoryImpl: ListingRepositoryImpl
    ): ListingRepository

    @Binds
    abstract fun bindShowRepository(
        showRepositoryImpl: ShowRepositoryImpl
    ): ShowRepository

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindDownloadRepository(
        downloadRepositoryImpl: DownloadRepositoryImpl
    ): DownloadRepository
}