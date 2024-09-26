package com.elfennani.aniwatch.data.remote.converters

import com.elfennani.anilist.type.MediaSeason
import com.elfennani.aniwatch.domain.models.enums.ShowSeason

fun MediaSeason.asAppModel() = when(this){
    MediaSeason.WINTER -> ShowSeason.WINTER
    MediaSeason.SPRING -> ShowSeason.SPRING
    MediaSeason.SUMMER -> ShowSeason.SUMMER
    MediaSeason.FALL -> ShowSeason.FALL
    MediaSeason.UNKNOWN__ -> TODO()
}