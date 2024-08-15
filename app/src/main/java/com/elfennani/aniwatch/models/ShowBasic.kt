package com.elfennani.aniwatch.models

data class ShowBasic(
    val id: Int,
    val name: String,
    val status: ShowStatus?,
    val description: String,
    val episodes: Int,
    val progress: Int?,
    val image: ShowImage,
    val banner: String?
)