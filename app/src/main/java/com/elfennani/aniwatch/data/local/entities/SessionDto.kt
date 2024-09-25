package com.elfennani.aniwatch.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elfennani.aniwatch.domain.models.Session
import java.util.Date

@Entity(tableName = "session")
data class SessionEntity(
    @PrimaryKey(true) val id: Int,
    val token: String,
    val expiration: Long
)

fun SessionEntity.asDomain() = Session(
    id=this.id,
    expiration=Date(this.expiration),
    token=this.token
)