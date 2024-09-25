package com.elfennani.aniwatch.domain.models

data class Character(
    val id: Int,
    val name: String,
    val image: String?,
    val imageSD: String?,
    val role: String,
    val voiceActor: List<VoiceActor>
)