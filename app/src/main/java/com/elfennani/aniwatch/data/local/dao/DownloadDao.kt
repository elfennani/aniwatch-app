package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.elfennani.aniwatch.data.local.entities.DownloadDto
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {
    @Query("SELECT * FROM downloads WHERE status='PENDING' ORDER BY createdAt ASC LIMIT 1")
    suspend fun getLatestDownload(): DownloadDto?

    @Query("SELECT * FROM downloads ORDER BY createdAt")
    fun getDownloadsFlow(): Flow<List<DownloadDto>>

    @Query("DELETE FROM downloads WHERE showId=:showId AND episode=:episode")
    suspend fun deleteDownload(showId: Int, episode: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDownload(download: DownloadDto)

    @Query("UPDATE downloads SET status=:status")
    suspend fun updateDownloadStatus(status: String)

    @Update
    suspend fun updateDownload(download: DownloadDto)

    @Query("DELETE FROM downloads")
    suspend fun deleteAll()

    @Query("SELECT * FROM downloads WHERE status='COMPLETED'")
    fun getDownloadedFlow(): Flow<List<DownloadDto>>

    @Query("SELECT * FROM downloads WHERE status='COMPLETED'")
    suspend fun getDownloaded(): List<DownloadDto>
}