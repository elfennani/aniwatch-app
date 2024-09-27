package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.elfennani.aniwatch.data.local.models.LocalUser
import com.elfennani.aniwatch.data.local.relations.SessionUser
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Upsert
    suspend fun upsert(user: LocalUser)

    @Upsert
    suspend fun upsert(users: List<LocalUser>)

    @Query("SELECT * FROM LocalUser WHERE id=:id")
    suspend fun getById(id: Int): LocalUser?

    @Query("SELECT * FROM LocalUser WHERE id=:id")
    fun getByIdFlow(id: Int): Flow<LocalUser>

    @Transaction
    @Query("SELECT * FROM LocalSession LIMIT 1")
    fun getCurrentUser(): Flow<SessionUser>
}