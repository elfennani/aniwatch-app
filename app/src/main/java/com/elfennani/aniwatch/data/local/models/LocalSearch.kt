package com.elfennani.aniwatch.data.local.models

import androidx.room.Entity

@Entity(primaryKeys = ["showId", "query"])
data class LocalSearch(
    val showId: Int,
    val query: String,
    val page: Int,
)
