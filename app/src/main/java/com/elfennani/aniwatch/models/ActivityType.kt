package com.elfennani.aniwatch.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class ActivityType {
    TEXT, LIST
}