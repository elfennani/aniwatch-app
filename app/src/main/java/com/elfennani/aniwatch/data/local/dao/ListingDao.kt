package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.elfennani.aniwatch.data.local.entities.ListingItemEntity
import com.elfennani.aniwatch.models.ShowStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ListingDao {
    @Query("SELECT * FROM cached_listing WHERE status=:status ORDER BY updatedAt DESC")
    fun getShowsByStatusFlow(status: ShowStatus): Flow<List<ListingItemEntity>>

    @Query("SELECT * FROM cached_listing WHERE status=:status ORDER BY updatedAt DESC")
    suspend fun getShowsByStatus(status: ShowStatus): List<ListingItemEntity>

    @Query("DELETE FROM cached_listing WHERE status=:status")
    suspend fun deleteByStatus(status: ShowStatus)

    @Upsert
    suspend fun upsertAll(shows: List<ListingItemEntity>)

    @Query("DELETE FROM cached_listing WHERE status=:status AND id NOT IN (:idList)")
    suspend fun deleteUnused(status: ShowStatus, idList: List<Int>)
}