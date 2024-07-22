package com.elfennani.aniwatch.data.remote.models

import com.elfennani.aniwatch.domain.models.ShowSeason

enum class SerializableShowSeason {
    WINTER,
    SPRING,
    SUMMER,
    FALL
}

fun SerializableShowSeason.toDomain(): ShowSeason {
    return ShowSeason.valueOf(this.name)
}