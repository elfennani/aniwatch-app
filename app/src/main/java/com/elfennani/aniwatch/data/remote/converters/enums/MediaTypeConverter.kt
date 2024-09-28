package com.elfennani.aniwatch.data.remote.converters.enums

import com.elfennani.anilist.type.MediaType
import com.elfennani.aniwatch.domain.models.enums.MediaType as AppMediaType

fun MediaType.asAppModel() = when(this){
    MediaType.ANIME -> AppMediaType.ANIME
    MediaType.MANGA -> AppMediaType.MANGA
    MediaType.UNKNOWN__ -> throw Exception()
}