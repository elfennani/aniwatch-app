package com.elfennani.aniwatch.data.repository

import androidx.room.withTransaction
import com.apollographql.apollo.ApolloClient
import com.elfennani.anilist.ShowByIdQuery
import com.elfennani.anilist.ShowRelationsQuery
import com.elfennani.aniwatch.data.local.AppDatabase
import com.elfennani.aniwatch.data.local.dao.RelationDao
import com.elfennani.aniwatch.data.local.dao.ShowDao
import com.elfennani.aniwatch.data.local.models.LocalMediaRelation
import com.elfennani.aniwatch.data.local.models.LocalShow
import com.elfennani.aniwatch.data.local.models.asAppModel
import com.elfennani.aniwatch.data.remote.converters.asAppModel
import com.elfennani.aniwatch.data.remote.converters.asEntity
import com.elfennani.aniwatch.di.AniListApolloClient
import com.elfennani.aniwatch.domain.models.Character
import com.elfennani.aniwatch.domain.models.Show
import com.elfennani.aniwatch.domain.models.StatusDetails
import com.elfennani.aniwatch.domain.models.enums.RelationType
import com.elfennani.aniwatch.domain.repositories.ShowRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ShowRepositoryImpl(
    @AniListApolloClient private val apolloClient: ApolloClient,
    private val showDao: ShowDao,
    private val relationDao: RelationDao,
    private val database: AppDatabase,
) : ShowRepository {
    override fun showById(id: Int) = showDao.getByIdFlow(id).map { it.asAppModel() }

    override suspend fun fetchShowById(id: Int) {
        withContext(Dispatchers.IO) {
            val response = apolloClient.query(ShowByIdQuery(id)).execute()
            val show = response.data?.media?.showFragment?.asEntity() ?: return@withContext

            showDao.upsert(show)
        }
    }

    override fun relationsById(showId: Int): Flow<List<Pair<RelationType, Show>>> {
        return relationDao
            .getRelationsByShowId(showId)
            .map { relations ->
                val shows = showDao.getByIdList(relations.map { it.showId }.distinct())

                relations.map { relation ->
                    val show = shows.find { it.id == relation.showId }!!.asAppModel()
                    Pair(relation.type, show)
                }
            }
    }

    override suspend fun fetchRelationsById(showId: Int) {
        withContext(Dispatchers.IO) {
            val response = apolloClient.query(ShowRelationsQuery(showId)).execute()
            val relations = response.data?.media?.relations?.edges

            val shows: List<LocalShow> = relations?.mapNotNull {
                it?.relationFragment?.node?.showFragment?.asEntity()
            } ?: emptyList()

            val localRelations = relations?.mapNotNull {
                LocalMediaRelation(
                    id = it?.relationFragment?.id!!,
                    parentShowId = showId,
                    showId = it.relationFragment.node?.showFragment?.id!!,
                    type = it.relationFragment.relationType?.asAppModel()!!
                )
            } ?: emptyList()

            database.withTransaction {
                relationDao.deleteByParentShowId(showId)

                showDao.upsert(shows)
                relationDao.upsert(localRelations)
            }
        }
    }

    override fun charactersById(showId: Int): Flow<List<Character>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchCharactersById(showId: Int) {
        TODO("Not yet implemented")
    }
}