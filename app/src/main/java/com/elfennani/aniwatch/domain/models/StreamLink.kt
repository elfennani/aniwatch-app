package com.elfennani.aniwatch.domain.models

data class StreamLink(
    val url: String,
    val resolutions: Map<String, String>
)
