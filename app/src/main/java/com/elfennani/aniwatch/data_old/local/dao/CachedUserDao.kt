package com.elfennani.aniwatch.data_old.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.elfennani.aniwatch.data_old.local.entities.CachedUser
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedUserDao {
    @Upsert
    suspend fun upsertUser(user: CachedUser)

    @Query("SELECT * FROM cached_user WHERE id=:id")
    fun getUserFlow(id: Int): Flow<CachedUser?>

    @Query("SELECT * FROM cached_user WHERE id=:id")
    suspend fun getUser(id: Int): CachedUser?
}