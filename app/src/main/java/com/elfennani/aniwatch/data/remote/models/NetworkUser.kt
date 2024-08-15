package com.elfennani.aniwatch.data.remote.models

import com.elfennani.aniwatch.models.User
import com.elfennani.aniwatch.models.UserAnimeStats
import com.elfennani.aniwatch.models.UserMangaStats
import com.squareup.moshi.Json

data class NetworkUser(
    val id: Int,
    val icon: String?,
    @Json(name = "icon_large") val iconLarge: String?,
    val banner: String?,
    val name: String,
    val bio: String?,

    @Json(name = "anime_watched") val animeWatched: Int,
    @Json(name = "anime_days_watched") val animeDaysWatched: Float,
    @Json(name = "anime_mean_score") val animeMeanScore: Float,

    @Json(name = "manga_read") val mangaRead: Int,
    @Json(name = "manga_chapters_read") val mangaChaptersRead: Int,
    @Json(name = "manga_mean_score") val mangaMeanScore: Float,
)

fun NetworkUser.toDomain() = User(
    id = id,
    icon = icon,
    iconLarge = iconLarge,
    banner = banner,
    name = name,
    bio = bio,
    animeStats = UserAnimeStats(
        watched = animeWatched,
        daysWatched = animeDaysWatched,
        meanScore = animeMeanScore
    ),
    mangaStats = UserMangaStats(
        read = mangaRead,
        chaptersRead = mangaChaptersRead,
        meanScore = mangaMeanScore
    )
)