package com.elfennani.aniwatch.models

data class StreamLink(
    val url: String,
    val resolutions: Map<String, String>
)
