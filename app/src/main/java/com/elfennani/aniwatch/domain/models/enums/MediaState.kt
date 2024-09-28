package com.elfennani.aniwatch.domain.models.enums

enum class MediaState(val value: String) {
    FINISHED("Finished"),
    RELEASING("Releasing"),
    NOT_YET_RELEASED("Not released yet"),
    CANCELLED("Cancelled"),
    HIATUS("Hiatus"),
}