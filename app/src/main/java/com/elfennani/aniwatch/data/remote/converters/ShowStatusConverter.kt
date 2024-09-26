package com.elfennani.aniwatch.data.remote.converters

import com.elfennani.anilist.type.MediaListStatus
import com.elfennani.aniwatch.domain.models.enums.ShowStatus

fun MediaListStatus.asAppModel() = when(this){
    MediaListStatus.CURRENT -> ShowStatus.WATCHING
    MediaListStatus.PLANNING -> ShowStatus.PLAN_TO_WATCH
    MediaListStatus.COMPLETED -> ShowStatus.COMPLETED
    MediaListStatus.DROPPED -> ShowStatus.DROPPED
    MediaListStatus.PAUSED -> ShowStatus.ON_HOLD
    MediaListStatus.REPEATING -> ShowStatus.REPEATING
    MediaListStatus.UNKNOWN__ -> null
}