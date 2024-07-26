package com.elfennani.aniwatch.presentation.screens.episode

import androidx.compose.runtime.Stable
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.extractor.mp4.Track
import com.elfennani.aniwatch.models.Episode
import com.elfennani.aniwatch.models.EpisodeLink
import com.elfennani.aniwatch.models.ShowDetails

@Stable
data class EpisodeUiState(
    val episode: EpisodeLink? = null,
    val show: ShowDetails? = null,
    val episodeDetails: Episode? = null,
    val loading: Boolean = false,
    val error: String? = null,
    val exoPlayer: ExoPlayer? = null,
    val trackGroup: Tracks.Group? = null,
    val currentPosition: Long? = null,
)
