package com.elfennani.aniwatch.data.local.mappers

import com.elfennani.aniwatch.data.local.entities.UserEntity
import com.elfennani.aniwatch.models.User
import com.elfennani.aniwatch.models.UserAnimeStats
import com.elfennani.aniwatch.models.UserMangaStats

fun UserEntity.toDomain() = User(
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

fun User.toEntity() = UserEntity(
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