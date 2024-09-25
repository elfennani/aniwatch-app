package com.elfennani.aniwatch.domain.models

data class ActivtyShow(
    val id: Int,
    val name: String,
    val image: String,
    val type: MediaType,
    val year: Int?
)
