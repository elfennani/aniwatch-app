package com.elfennani.aniwatch.data.local.models

import com.elfennani.aniwatch.domain.models.UserAnimeStats

data class EmbeddedAnimeStats(
    val count: Int,
    val daysWatched: Float,
    val meanScore: Float,
)

fun EmbeddedAnimeStats.asAppModel() = UserAnimeStats(
    watched = count,
    daysWatched = daysWatched,
    meanScore = meanScore
)