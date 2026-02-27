package com.elfennani.aniwatch.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.models.ShowSeason
import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.models.Tag

data class EmbeddedShowImage(
    val large: String,
    val medium: String,
    val original: String,
    val color: String?
)

@Entity(tableName = "cached_shows")
data class ShowEntity(
    @PrimaryKey val id: Int,
    val allanimeId: String,
    val name: String,
    val description: String,
    val episodesCount: Int,
    val genres: List<String>,
    val season: ShowSeason,
    val year: Int,
    val format: String,
    @Embedded val image: EmbeddedShowImage,
    val banner: String?,
    val progress: Int?,
    val status: ShowStatus?,
    @ColumnInfo(defaultValue = "[]") val tags: List<Tag>
)