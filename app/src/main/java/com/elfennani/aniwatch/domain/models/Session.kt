package com.elfennani.aniwatch.domain.models

import kotlinx.datetime.Instant


data class Session(
    val id: Int,
    val token: String,
    val expiration: Instant
)
