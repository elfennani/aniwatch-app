package com.elfennani.aniwatch.data.remote.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class TranslationNetwork {
    SUB,
    DUB;

    override fun toString(): String {
        return this.name.lowercase()
    }
}