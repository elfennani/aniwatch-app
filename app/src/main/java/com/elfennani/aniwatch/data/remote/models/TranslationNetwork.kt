package com.elfennani.aniwatch.data.remote.models

enum class TranslationNetwork {
    SUB,
    DUB;

    override fun toString(): String {
        return this.name.lowercase()
    }
}