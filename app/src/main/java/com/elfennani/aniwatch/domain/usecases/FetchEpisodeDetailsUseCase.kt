package com.elfennani.aniwatch.domain.usecases

import com.elfennani.aniwatch.domain.errors.AppError
import com.elfennani.aniwatch.domain.errors.AppError.Companion.handleAppErrors
import com.elfennani.aniwatch.domain.models.EpisodeAudio
import com.elfennani.aniwatch.domain.models.EpisodeDetails
import com.elfennani.aniwatch.domain.models.Resource
import com.elfennani.aniwatch.domain.repositories.EpisodeRepository
import javax.inject.Inject

class FetchEpisodeDetailsUseCase @Inject constructor(private val episodeRepository: EpisodeRepository) {
    suspend operator fun invoke(
        showId: Int,
        episode: Double,
        audio: EpisodeAudio,
        onlyMp4: Boolean = false,
    ): Resource<EpisodeDetails, AppError> {
        return try {
            val episodeDetails = episodeRepository.fetchEpisodeUrl(showId, episode, audio, onlyMp4)
            Resource.Ok(episodeDetails)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Err(e.handleAppErrors())
        }
    }
}