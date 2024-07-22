package com.elfennani.aniwatch.domain.models

import java.util.Date

data class Session(
    val id: Int,
    val token: String,
    val expiration: Date
)
