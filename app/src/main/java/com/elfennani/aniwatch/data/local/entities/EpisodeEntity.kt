package com.elfennani.aniwatch.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.models.DownloadState
import com.elfennani.aniwatch.models.Episode

@Entity("cached_episodes")
data class EpisodeEntity(
    @PrimaryKey val id: String,
    val allanimeId: String,
    val animeId: Int,
    val episode: Double,
    val name: String,
    val dubbed: Boolean,
    val thumbnail: String?,
    val duration: Int?,
)