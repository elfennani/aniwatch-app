package com.elfennani.aniwatch.data_old.remote.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class TranslationNetwork {
    SUB,
    DUB;

    override fun toString(): String {
        return this.name.lowercase()
    }
}