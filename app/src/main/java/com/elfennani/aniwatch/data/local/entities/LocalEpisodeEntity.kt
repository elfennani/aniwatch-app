package com.elfennani.aniwatch.data.local.entities

import androidx.room.Entity

@Entity(
    "local_episodes",
    primaryKeys = ["showId", "episode"]
)
data class LocalEpisodeEntity(
    val showId: Int,
    val episode: Double,
    val uri: String,
)
