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

fun ShowStatus.asRemoteModel() = when(this){
    ShowStatus.WATCHING -> MediaListStatus.CURRENT
    ShowStatus.PLAN_TO_WATCH -> MediaListStatus.PLANNING
    ShowStatus.COMPLETED -> MediaListStatus.COMPLETED
    ShowStatus.DROPPED -> MediaListStatus.DROPPED
    ShowStatus.ON_HOLD -> MediaListStatus.PAUSED
    ShowStatus.REPEATING -> MediaListStatus.REPEATING
}