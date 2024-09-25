package com.elfennani.aniwatch.data_old.remote.models

import com.elfennani.aniwatch.domain.models.StreamLink
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkHlsLink(
    val originalUrl: String,
    val resolutions: Map<String, String>
)

fun NetworkHlsLink.toDomain() = StreamLink(
    url = this.originalUrl,
    resolutions = this.resolutions
)