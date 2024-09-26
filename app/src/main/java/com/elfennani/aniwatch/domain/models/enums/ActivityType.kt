package com.elfennani.aniwatch.domain.models.enums

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class ActivityType {
    TEXT, LIST
}