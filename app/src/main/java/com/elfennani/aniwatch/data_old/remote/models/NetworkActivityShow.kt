package com.elfennani.aniwatch.data_old.remote.models

import com.elfennani.aniwatch.domain.models.MediaType
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkActivityShow(
    val id: Int,
    val name: String,
    val image: String,
    val type: String,
    val year: Int?,
)

fun NetworkActivityShow.asDomain() = com.elfennani.aniwatch.domain.models.ActivtyShow(
    id = id,
    name = name,
    image = image,
    type = MediaType.valueOf(type),
    year = year
)