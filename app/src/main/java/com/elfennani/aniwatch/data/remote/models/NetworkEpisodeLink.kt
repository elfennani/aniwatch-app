package com.elfennani.aniwatch.data.remote.models

import com.elfennani.aniwatch.models.EpisodeLink
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkEpisodeLink(
    val hls: NetworkHlsLink?,
    val mp4: String?,
    val dubbed: Boolean,
)

fun NetworkEpisodeLink.toDomain() = EpisodeLink(
    mp4 = this.mp4,
    hls = this.hls?.toDomain(),
    dubbed = this.dubbed
)