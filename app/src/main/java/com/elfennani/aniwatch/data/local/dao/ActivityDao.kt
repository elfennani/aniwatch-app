package com.elfennani.aniwatch.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.elfennani.aniwatch.data.local.models.LocalActivity

@Dao
interface ActivityDao {

    @Upsert
    suspend fun upsert(activities: List<LocalActivity>)

    @Query("DELETE FROM LocalActivity")
    suspend fun deleteAll()

    @Query("SELECT * FROM LocalActivity ORDER BY id DESC")
    fun pagingSource(): PagingSource<Int, LocalActivity>

}