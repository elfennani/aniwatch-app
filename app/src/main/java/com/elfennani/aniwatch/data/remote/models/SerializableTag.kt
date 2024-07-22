package com.elfennani.aniwatch.data.remote.models

import com.elfennani.aniwatch.domain.models.Tag

data class SerializableTag(
    val id: Int,
    val label: String,
    val percentage: Int,
    val spoiler: Boolean,
)

fun SerializableTag.toDomain() = Tag(
    id = this.id,
    label = this.label,
    percentage = this.percentage,
    spoiler = this.spoiler
)