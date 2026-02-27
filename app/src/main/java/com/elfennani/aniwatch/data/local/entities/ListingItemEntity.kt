package com.elfennani.aniwatch.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.data.remote.models.NetworkShowBasic
import com.elfennani.aniwatch.data.remote.models.toDomain
import com.elfennani.aniwatch.models.ShowBasic
import com.elfennani.aniwatch.models.ShowImage
import com.elfennani.aniwatch.models.ShowStatus
import com.elfennani.aniwatch.utils.toColor
import com.elfennani.aniwatch.utils.toHexString

@Entity(tableName = "cached_listing")
data class ListingItemEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: ShowStatus?,
    val description: String?,
    val episodes: Int?,
    val progress: Int?,
    @Embedded val image: EmbeddedShowImage,
    val banner: String? = null,
    val updatedAt: Int?
)