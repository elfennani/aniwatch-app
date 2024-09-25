package com.elfennani.aniwatch.domain.models

data class ActivtyShow(
    val id: Int,
    val name: String,
    val image: String,
    val type: com.elfennani.aniwatch.domain.models.MediaType,
    val year: Int?
)
