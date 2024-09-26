package com.elfennani.aniwatch.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.elfennani.aniwatch.data.local.models.LocalMediaRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface RelationDao {

    @Upsert
    suspend fun upsert(relation: LocalMediaRelation)

    @Upsert
    suspend fun upsert(relations: List<LocalMediaRelation>)

    @Query("DELETE FROM LocalMediaRelation WHERE parentShowId=:showId")
    suspend fun deleteByParentShowId(showId: Int)

    @Query("SELECT * FROM LocalMediaRelation WHERE parentShowId=:showId")
    fun getRelationsByShowId(showId: Int): Flow<List<LocalMediaRelation>>

}