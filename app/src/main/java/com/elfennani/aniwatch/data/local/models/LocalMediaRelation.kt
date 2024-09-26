package com.elfennani.aniwatch.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.domain.models.enums.RelationType

@Entity
data class LocalMediaRelation(
    @PrimaryKey val id: Int,
    val parentShowId: Int,
    val showId: Int,
    val type: RelationType
)
