package com.elfennani.aniwatch.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elfennani.aniwatch.data.local.entities.ActivityDto


@Dao
interface FeedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<ActivityDto>)

    @Query("SELECT * FROM feed ORDER BY createdAt DESC")
    fun pagingSource(): PagingSource<Int, ActivityDto>

    @Query("DELETE FROM feed")
    fun clearAll()
}