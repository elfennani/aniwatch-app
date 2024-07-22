package com.elfennani.aniwatch.data.remote.models

import com.elfennani.aniwatch.domain.models.ShowBasic

data class SerializableShowBasic(
    val id: Int,
    val name: String,
    val status: SerializableShowStatus?,
    val description: String,
    val episodes: Int,
    val progress: Int?,
    val image: SerializableShowImage
)

fun SerializableShowBasic.toDomain() = ShowBasic(
    id = this.id,
    name = this.name,
    description = this.description,
    status = this.status?.toDomain(),
    image = this.image.toDomain(),
    progress = this.progress,
    episodes = this.episodes
)