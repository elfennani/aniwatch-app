package com.elfennani.aniwatch.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elfennani.aniwatch.data.local.entities.ActivityEntity


@Dao
interface FeedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<ActivityEntity>)

    @Query("SELECT * FROM feed ORDER BY createdAt DESC")
    fun pagingSource(): PagingSource<Int, ActivityEntity>

    @Query("DELETE FROM feed")
    fun clearAll()
}