package com.elfennani.aniwatch.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.domain.models.Character
import com.elfennani.aniwatch.domain.models.VoiceActor
import com.elfennani.aniwatch.domain.models.enums.CharacterRole

@Entity
data class LocalCharacter(
    @PrimaryKey val id: Int,
    val showId: Int,
    val name: String,
    val role: CharacterRole,
    val icon: String?,
    val iconHD: String?,
    val voiceActors: List<VoiceActor>
)

fun LocalCharacter.asAppModel() = Character(
    id = id,
    name = name,
    image = iconHD,
    imageSD = icon,
    role = role,
    voiceActors = voiceActors
)