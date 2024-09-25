package com.elfennani.aniwatch.data_old.remote.models

import com.elfennani.aniwatch.domain.models.Tag
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
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