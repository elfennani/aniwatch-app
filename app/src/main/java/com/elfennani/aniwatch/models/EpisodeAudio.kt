package com.elfennani.aniwatch.models

import com.elfennani.aniwatch.data.remote.models.TranslationNetwork

enum class EpisodeAudio {
    DUB, SUB
}

fun EpisodeAudio.toNetwork() = TranslationNetwork.valueOf(this.name)