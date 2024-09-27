package com.elfennani.aniwatch.domain.models

import com.elfennani.aniwatch.domain.models.enums.CharacterRole

data class Character(
    val id: Int,
    val name: String,
    val image: String?,
    val imageSD: String?,
    val role: CharacterRole,
    val voiceActors: List<VoiceActor>
)