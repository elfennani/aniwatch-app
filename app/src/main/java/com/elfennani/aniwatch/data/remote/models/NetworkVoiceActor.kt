package com.elfennani.aniwatch.data.remote.models

import com.elfennani.aniwatch.domain.models.VoiceActor

data class NetworkVoiceActor(
    val id: Int,
    val name: String,
    val image: String?,
    val language: String?
)

fun NetworkVoiceActor.asDomain() = VoiceActor(
    id = id,
    name = name,
    image = image,
    language = language
)