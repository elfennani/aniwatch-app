package com.elfennani.aniwatch.models

data class VoiceActor(
    val id: Int,
    val name: String,
    val image: String?,
    val language: String?
)

fun List<VoiceActor>.languages(): Set<String> = this.mapNotNull { it.language }.toSet()