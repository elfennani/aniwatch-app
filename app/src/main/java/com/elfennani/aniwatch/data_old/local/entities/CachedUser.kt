package com.elfennani.aniwatch.data_old.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.domain.models.User
import com.elfennani.aniwatch.domain.models.UserAnimeStats
import com.elfennani.aniwatch.domain.models.UserMangaStats

@Entity(tableName = "cached_user")
data class CachedUser(
    @PrimaryKey val id: Int,
    val icon: String?,
    val iconLarge: String?,
    val banner: String?,
    val name: String,
    val bio: String?,

    val animeWatched: Int,
    val animeDaysWatched: Float,
    val animeMeanScore: Float,

    val mangaRead: Int,
    val mangaChaptersRead: Int,
    val mangaMeanScore: Float,
)

fun CachedUser.toDomain() = User(
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

fun User.toEntity() = CachedUser(
    id = id,
    icon = icon,
    iconLarge = iconLarge,
    banner = banner,
    name = name,
    bio = bio,
    animeWatched = animeStats.watched,
    animeDaysWatched = animeStats.daysWatched,
    animeMeanScore = animeStats.meanScore,
    mangaRead = mangaStats.read,
    mangaChaptersRead = mangaStats.chaptersRead,
    mangaMeanScore = mangaStats.meanScore
)