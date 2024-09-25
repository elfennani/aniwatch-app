package com.elfennani.aniwatch.domain.models

import com.squareup.moshi.Json

data class User(
    val id: Int,
    val icon: String?,
    val iconLarge: String?,
    val banner: String?,
    val name: String,
    val bio: String?,

    val animeStats: UserAnimeStats,
    val mangaStats: UserMangaStats
)
