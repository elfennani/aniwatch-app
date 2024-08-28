package com.elfennani.aniwatch.models

import androidx.annotation.Keep
import com.elfennani.aniwatch.data.remote.models.TranslationNetwork

@Keep
enum class EpisodeAudio {
    DUB, SUB
}

fun EpisodeAudio.toNetwork() = TranslationNetwork.valueOf(this.name)