package com.elfennani.aniwatch.domain.models

import androidx.annotation.Keep
import com.elfennani.aniwatch.data_old.remote.models.TranslationNetwork

@Keep
enum class EpisodeAudio {
    DUB, SUB
}

fun EpisodeAudio.toNetwork() = TranslationNetwork.valueOf(this.name)