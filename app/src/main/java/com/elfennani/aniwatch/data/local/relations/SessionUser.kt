package com.elfennani.aniwatch.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.elfennani.aniwatch.data.local.models.LocalSession
import com.elfennani.aniwatch.data.local.models.LocalUser

data class SessionUser(
    @Embedded val session: LocalSession,

    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
    val user: LocalUser
)
