package com.elfennani.aniwatch.domain.models

import com.elfennani.aniwatch.data.remote.models.NetworkShowBasic
import com.squareup.moshi.Json

data class Relation(
    val id: Int,
    val relationType: String,
    val format: String?,
    val state: String?,
    val show: ShowBasic
)
