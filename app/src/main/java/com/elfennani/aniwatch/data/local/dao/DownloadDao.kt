package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.elfennani.aniwatch.data.local.entities.LocalDownloadState
import com.elfennani.aniwatch.data.local.entities.DownloadedEpisodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {
    @Query("SELECT * FROM downloaded_episodes ORDER BY createdAt DESC")
    fun getDownloads(): Flow<List<DownloadedEpisodeEntity>>

    @Upsert
    suspend fun upsertDownload(downloadedEpisode: DownloadedEpisodeEntity)

    @Query("UPDATE downloaded_episodes SET progress=:progress, state=:state WHERE showId=:showId AND episode=:episode")
    suspend fun updateProgress(
        showId: Int,
        episode: kotlin.Double,
        progress: Float,
        state: LocalDownloadState = LocalDownloadState.DOWNLOADING
    )

    @Query("UPDATE downloaded_episodes SET state=:state WHERE showId=:showId AND episode=:episode")
    suspend fun updateState(
        showId: Int,
        episode: kotlin.Double,
        state: LocalDownloadState,
    )

    @Query("UPDATE downloaded_episodes SET errorRes=:errorRes, state=:state WHERE showId=:showId AND episode=:episode")
    suspend fun updateError(
        showId: Int,
        episode: kotlin.Double,
        errorRes: Int?,
        state: LocalDownloadState = LocalDownloadState.FAILURE
    )

    @Delete
    suspend fun deleteDownload(downloadedEpisode: DownloadedEpisodeEntity)

    @Query("DELETE FROM downloaded_episodes WHERE showId=:showId AND episode=:episode")
    suspend fun deleteDownloadByShowId(showId: Int, episode: kotlin.Double)
}