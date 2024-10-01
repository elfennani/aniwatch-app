package com.elfennani.aniwatch.domain.models

data class Relation(
    val id: Int,
    val relationType: String,
    val format: String?,
    val state: String?,
    val show: Show
)
