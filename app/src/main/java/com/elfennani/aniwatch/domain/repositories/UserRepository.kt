package com.elfennani.aniwatch.domain.repositories

import com.elfennani.aniwatch.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val viewer: Flow<User>
    suspend fun fetchViewer()

    fun userById(id: Int): Flow<User>
    suspend fun fetchUserById(id: Int)
}