package com.elfennani.aniwatch.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.domain.models.Session
import kotlinx.datetime.Instant

@Entity
data class LocalSession(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val token: String,
    val expiration: Long,
    val userId: Int
)

fun LocalSession.asDomain() = Session(
    id=id!!,
    expiration= Instant.fromEpochMilliseconds(expiration),
    token=this.token,
    userId = userId
)