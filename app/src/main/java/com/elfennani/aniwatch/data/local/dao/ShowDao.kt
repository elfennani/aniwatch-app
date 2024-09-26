package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.elfennani.aniwatch.data.local.models.LocalMediaRelation
import com.elfennani.aniwatch.data.local.models.LocalShow
import com.elfennani.aniwatch.domain.models.enums.ShowStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowDao {
    @Upsert
    suspend fun upsert(show: LocalShow)

    @Upsert
    suspend fun upsert(shows:List<LocalShow>)

    @Query("SELECT * FROM LocalShow WHERE id=:id")
    suspend fun getById(id:Int): LocalShow?

    @Query("SELECT * FROM LocalShow WHERE id in (:idList)")
    suspend fun getByIdList(idList:List<Int>): List<LocalShow>

    @Query("SELECT * FROM LocalShow WHERE id=:id")
    fun getByIdFlow(id:Int): Flow<LocalShow>

    @Query("SELECT * FROM LocalShow WHERE status=:status")
    fun getByStatusFlow(status: ShowStatus): Flow<List<LocalShow>>

    @Query("DELETE FROM LocalShow WHERE status=:status")
    suspend fun deleteByStatus(status: ShowStatus)
}