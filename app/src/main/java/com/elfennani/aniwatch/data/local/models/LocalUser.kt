package com.elfennani.aniwatch.data.local.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.domain.models.User

@Entity
data class LocalUser(
    @PrimaryKey val id: Int,
    val icon: String?,
    val iconHD: String?,
    val banner: String?,
    val name: String,
    val bio: String?,

    @Embedded("anime_") val animeStats: EmbeddedAnimeStats,
    @Embedded("manga_") val mangaStats: EmbeddedMangaStats
)

fun LocalUser.asAppModel() = User(
    id = id,
    icon = icon,
    iconLarge = iconHD,
    banner = banner,
    name = name,
    bio = bio,
    animeStats = animeStats.asAppModel(),
    mangaStats = mangaStats.asAppModel()
)