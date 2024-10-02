package com.elfennani.aniwatch.domain.repositories

import com.elfennani.aniwatch.domain.models.Episode
import com.elfennani.aniwatch.domain.models.EpisodeAudio
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {
    fun episodesByShowId(showId: Int): Flow<List<Episode>>
    suspend fun fetchEpisodesByShowId(showId: Int)
}