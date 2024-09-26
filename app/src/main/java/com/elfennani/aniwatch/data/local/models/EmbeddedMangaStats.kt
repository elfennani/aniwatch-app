package com.elfennani.aniwatch.data.local.models

import com.elfennani.aniwatch.domain.models.UserMangaStats

data class EmbeddedMangaStats(
    val count: Int,
    val readChapters: Int,
    val meanScore: Float
)

fun EmbeddedMangaStats.asAppModel() = UserMangaStats(
    read = count,
    chaptersRead = readChapters,
    meanScore = meanScore
)