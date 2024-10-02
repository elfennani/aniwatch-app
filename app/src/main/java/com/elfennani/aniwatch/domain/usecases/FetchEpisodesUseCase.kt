package com.elfennani.aniwatch.domain.usecases

import com.elfennani.aniwatch.domain.errors.AppError
import com.elfennani.aniwatch.domain.errors.AppError.Companion.handleAppErrors
import com.elfennani.aniwatch.domain.models.Resource
import com.elfennani.aniwatch.domain.repositories.EpisodeRepository
import javax.inject.Inject

class FetchEpisodesUseCase @Inject constructor(private val episodeRepository: EpisodeRepository) {
    suspend operator fun invoke(showId: Int): Resource<Unit, AppError> {
        return try {
            episodeRepository.fetchEpisodesByShowId(showId)
            Resource.Ok(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Err(e.handleAppErrors())
        }
    }
}