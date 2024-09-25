package com.elfennani.aniwatch.data.remote.models

import com.elfennani.aniwatch.domain.models.Character
import com.squareup.moshi.Json

data class NetworkCharacter(
    val id: Int,
    val name: String,
    val image: String?,
    @Json(name = "image_sd") val imageSD: String?,
    val role: String,
    @Json(name = "voice_actors") val voiceActor: List<NetworkVoiceActor>,
)

fun NetworkCharacter.asDomain() = com.elfennani.aniwatch.domain.models.Character(
    id = id,
    name = name,
    image = image,
    imageSD = imageSD,
    role = role,
    voiceActor = voiceActor.map { it.asDomain() }
)