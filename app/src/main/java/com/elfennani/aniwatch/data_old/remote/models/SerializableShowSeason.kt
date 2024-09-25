package com.elfennani.aniwatch.data_old.remote.models

import com.elfennani.aniwatch.domain.models.ShowSeason
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class SerializableShowSeason {
    WINTER,
    SPRING,
    SUMMER,
    FALL
}

fun SerializableShowSeason.toDomain(): ShowSeason {
    return ShowSeason.valueOf(this.name)
}