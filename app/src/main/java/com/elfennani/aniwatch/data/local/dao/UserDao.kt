package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.elfennani.aniwatch.data.local.models.LocalUser

@Dao
interface UserDao {
    @Upsert
    suspend fun upsert(user: LocalUser)

    @Upsert
    suspend fun upsert(users: List<LocalUser>)

    @Query("SELECT * FROM LocalUser WHERE id=:id")
    suspend fun getById(id: Int): LocalUser?
}