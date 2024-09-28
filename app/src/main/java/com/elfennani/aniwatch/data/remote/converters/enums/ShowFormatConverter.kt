package com.elfennani.aniwatch.data.remote.converters.enums

import com.elfennani.anilist.type.MediaFormat
import com.elfennani.aniwatch.domain.models.enums.ShowFormat

fun MediaFormat.asAppModel() = when(this){
    MediaFormat.TV -> ShowFormat.TV
    MediaFormat.TV_SHORT -> ShowFormat.TV_SHORT
    MediaFormat.MOVIE -> ShowFormat.MOVIE
    MediaFormat.SPECIAL -> ShowFormat.SPECIAL
    MediaFormat.OVA -> ShowFormat.OVA
    MediaFormat.ONA -> ShowFormat.ONA
    MediaFormat.MUSIC -> ShowFormat.MUSIC
    MediaFormat.MANGA -> ShowFormat.MANGA
    MediaFormat.NOVEL -> ShowFormat.NOVEL
    MediaFormat.ONE_SHOT -> ShowFormat.ONE_SHOT
    MediaFormat.UNKNOWN__ -> null
}