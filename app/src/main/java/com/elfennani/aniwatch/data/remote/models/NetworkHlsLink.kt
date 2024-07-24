package com.elfennani.aniwatch.data.remote.models

import com.elfennani.aniwatch.models.StreamLink

data class NetworkHlsLink(
    val originalUrl: String,
    val resolutions: Map<String, String>
)

fun NetworkHlsLink.toDomain() = StreamLink(
    url = this.originalUrl,
    resolutions = this.resolutions
)