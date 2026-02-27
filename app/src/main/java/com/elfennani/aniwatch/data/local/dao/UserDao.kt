package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.elfennani.aniwatch.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: UserEntity)

    @Query("SELECT * FROM cached_user WHERE id=:id")
    fun getUserFlow(id: Int): Flow<UserEntity?>

    @Query("SELECT * FROM cached_user WHERE id=:id")
    suspend fun getUser(id: Int): UserEntity?
}