package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.elfennani.aniwatch.data.local.entities.LocalDownloadState
import com.elfennani.aniwatch.data.local.entities.LocalDownloadedEpisode
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {
    @Query("SELECT * FROM downloaded_episodes ORDER BY createdAt DESC")
    fun getDownloads(): Flow<List<LocalDownloadedEpisode>>

    @Upsert
    suspend fun upsertDownload(downloadedEpisode: LocalDownloadedEpisode)

    @Query("UPDATE downloaded_episodes SET progress=:progress, state=:state WHERE showId=:showId AND episode=:episode")
    suspend fun updateProgress(
        showId: Int,
        episode: Int,
        progress: Float,
        state: LocalDownloadState = LocalDownloadState.DOWNLOADING
    )

    @Query("UPDATE downloaded_episodes SET state=:state WHERE showId=:showId AND episode=:episode")
    suspend fun updateState(
        showId: Int,
        episode: Int,
        state: LocalDownloadState,
    )

    @Query("UPDATE downloaded_episodes SET errorRes=:errorRes, state=:state WHERE showId=:showId AND episode=:episode")
    suspend fun updateError(
        showId: Int,
        episode: Int,
        errorRes: Int?,
        state: LocalDownloadState = LocalDownloadState.FAILURE
    )

    @Delete
    suspend fun deleteDownload(downloadedEpisode: LocalDownloadedEpisode)
}