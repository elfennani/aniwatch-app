package com.elfennani.aniwatch.data.remote.models

import com.elfennani.aniwatch.domain.models.Relation
import com.squareup.moshi.Json

data class NetworkRelation(
    val id: Int,
    @Json(name = "relation_type") val relationType: String,
    val format: String?,
    val state: String?,
    val show: NetworkShowBasic
)

fun NetworkRelation.asDomain() = Relation(
    id = id,
    relationType = relationType,
    format = format,
    state = state,
    show = show.toDomain()
)