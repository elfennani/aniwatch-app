package com.elfennani.aniwatch.data.repository

import com.apollographql.apollo.ApolloClient
import com.elfennani.anilist.UserQuery
import com.elfennani.anilist.ViewerQuery
import com.elfennani.aniwatch.data.local.dao.UserDao
import com.elfennani.aniwatch.data.local.models.asAppModel
import com.elfennani.aniwatch.data.remote.converters.asEntity
import com.elfennani.aniwatch.di.AniListApolloClient
import com.elfennani.aniwatch.domain.models.User
import com.elfennani.aniwatch.domain.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @AniListApolloClient private val apolloClient: ApolloClient,
    private val userDao: UserDao,
) : UserRepository {
    override val viewer: Flow<User>
        get() = userDao.getCurrentUser().map { it.user.asAppModel() }

    override suspend fun fetchViewer() {
        withContext(Dispatchers.IO) {
            val response = apolloClient.query(ViewerQuery()).execute()
            val user = response.data?.Viewer?.userFragment!!

            userDao.upsert(user.asEntity())
        }
    }

    override fun userById(id: Int): Flow<User> = userDao.getByIdFlow(id).map { it.asAppModel() }

    override suspend fun fetchUserById(id: Int) {
        withContext(Dispatchers.IO){
            val response = apolloClient.query(UserQuery(id)).execute()
            val user = response.data?.User?.userFragment!!

            userDao.upsert(user.asEntity())
        }
    }
}