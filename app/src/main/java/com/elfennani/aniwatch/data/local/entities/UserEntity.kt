package com.elfennani.aniwatch.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.models.User
import com.elfennani.aniwatch.models.UserAnimeStats
import com.elfennani.aniwatch.models.UserMangaStats

@Entity(tableName = "cached_user")
data class UserEntity(
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