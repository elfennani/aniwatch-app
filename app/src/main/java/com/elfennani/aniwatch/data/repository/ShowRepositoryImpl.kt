package com.elfennani.aniwatch.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import androidx.room.withTransaction
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.elfennani.anilist.CharactersQuery
import com.elfennani.anilist.ShowByIdQuery
import com.elfennani.anilist.ShowRelationsQuery
import com.elfennani.anilist.UpdateStatusMutation
import com.elfennani.anilist.type.MediaStatus
import com.elfennani.aniwatch.data.local.AppDatabase
import com.elfennani.aniwatch.data.local.dao.CharacterDao
import com.elfennani.aniwatch.data.local.dao.RelationDao
import com.elfennani.aniwatch.data.local.dao.ShowDao
import com.elfennani.aniwatch.data.local.models.LocalMediaRelation
import com.elfennani.aniwatch.data.local.models.LocalShow
import com.elfennani.aniwatch.data.local.models.asAppModel
import com.elfennani.aniwatch.data.remote.converters.asAppModel
import com.elfennani.aniwatch.data.remote.converters.asEntity
import com.elfennani.aniwatch.data.remote.converters.asRemoteModel
import com.elfennani.aniwatch.di.AniListApolloClient
import com.elfennani.aniwatch.domain.models.Character
import com.elfennani.aniwatch.domain.models.Show
import com.elfennani.aniwatch.domain.models.StatusDetails
import com.elfennani.aniwatch.domain.models.enums.RelationType
import com.elfennani.aniwatch.domain.models.enums.ShowStatus
import com.elfennani.aniwatch.domain.paging.CharactersMediator
import com.elfennani.aniwatch.domain.repositories.ShowRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ShowRepositoryImpl(
    @AniListApolloClient private val apolloClient: ApolloClient,
    private val showDao: ShowDao,
    private val relationDao: RelationDao,
    private val characterDao: CharacterDao,
    private val database: AppDatabase,
    private val dataStore: DataStore<Preferences>,
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

    @OptIn(ExperimentalPagingApi::class)
    override fun charactersById(showId: Int) = Pager(
        PagingConfig(pageSize = 25),
        remoteMediator = CharactersMediator(this, dataStore, showId)
    ) {
        characterDao.getPaging(showId)
    }.flow
        .map { it.map { character -> character.asAppModel() } }

    override suspend fun fetchCharactersById(showId: Int, page: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val response = apolloClient.query(CharactersQuery(showId, page)).execute()
            val hasNextPage =
                response.data?.media?.characters?.pageInfo?.pageInfoFragment?.hasNextPage
                    ?: false
            val characters = response.data?.media?.characters?.edges?.mapNotNull {
                it?.characterFragment?.asEntity(showId)
            } ?: emptyList()

            database.withTransaction {
                characterDao.deleteByShowId(showId)
                characterDao.upsert(characters)
            }

            return@withContext hasNextPage
        }
    }

    override suspend fun incrementEpisodeProgress(showId: Int) {
        withContext(Dispatchers.IO) {
            val response = apolloClient.query(ShowByIdQuery(showId)).execute()
            val show = response.data?.media?.showFragment?.asEntity()?.asAppModel()!!

            val isCurrentlyWatching =
                show.status in listOf(ShowStatus.REPEATING, ShowStatus.WATCHING)
            val isFinished = (show.progress ?: 0) >= (show.episodes ?: Int.MAX_VALUE)
            val willBeFinished = ((show.progress ?: 0) + 1) >= (show.episodes ?: Int.MAX_VALUE)

            if (!isCurrentlyWatching || isFinished)
                return@withContext

            val mutation = UpdateStatusMutation(
                mediaId = showId,
                progress = Optional.present((show.progress ?: 0) + 1),
                status = if (willBeFinished) {
                    Optional.present(ShowStatus.COMPLETED.asRemoteModel())
                } else {
                    Optional.absent()
                }
            )

            val mutationResponse = apolloClient.mutation(mutation).execute()

            mutationResponse.data?.SaveMediaListEntry?.media?.showFragment?.let {
                showDao.upsert(it.asEntity())
            }
        }
    }
}