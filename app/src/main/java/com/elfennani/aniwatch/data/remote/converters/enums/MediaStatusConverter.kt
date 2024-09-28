package com.elfennani.aniwatch.data.remote.converters.enums

import com.elfennani.aniwatch.domain.models.enums.MediaState
import com.elfennani.anilist.type.MediaStatus as AniListMediaStatus

fun AniListMediaStatus.asAppModel() = when(this){
    AniListMediaStatus.FINISHED -> MediaState.FINISHED
    AniListMediaStatus.RELEASING -> MediaState.RELEASING
    AniListMediaStatus.NOT_YET_RELEASED -> MediaState.NOT_YET_RELEASED
    AniListMediaStatus.CANCELLED -> MediaState.CANCELLED
    AniListMediaStatus.HIATUS -> MediaState.HIATUS
    AniListMediaStatus.UNKNOWN__ -> throw Exception()
}