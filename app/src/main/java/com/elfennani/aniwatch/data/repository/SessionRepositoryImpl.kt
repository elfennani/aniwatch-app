package com.elfennani.aniwatch.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import com.elfennani.anilist.ViewerQuery
import com.elfennani.aniwatch.R
import com.elfennani.aniwatch.data.local.dao.ActivityDao
import com.elfennani.aniwatch.data.local.dao.SessionDao
import com.elfennani.aniwatch.data.local.models.LocalSession
import com.elfennani.aniwatch.data.local.models.asDomain
import com.elfennani.aniwatch.domain.models.Session
import com.elfennani.aniwatch.domain.repositories.SessionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionDao: SessionDao,
    private val dataStore: DataStore<Preferences>,
    @ApplicationContext private val context: Context,
) : SessionRepository {
    override suspend fun getCurrentSession(): Session? {
        val sessionId = dataStore.data.first()[SessionRepository.SESSION_KEY] ?: return null
        val session = sessionDao.getSessionById(sessionId)

        return session.asDomain()
    }

    override suspend fun removeCurrentSession() {
        val sessionId = dataStore.data.first()[SessionRepository.SESSION_KEY] ?: return

        dataStore.edit { it.remove(SessionRepository.SESSION_KEY) }
        sessionDao.deleteSessionById(sessionId)
    }

    override suspend fun addNewSession(accessToken: String, expiration: Long) {
        val apolloClient = ApolloClient.Builder()
            .serverUrl(context.getString(R.string.anilist_graphql))
            .addHttpHeader("Authorization", "Bearer $accessToken")
            .build()
        val viewer = apolloClient.query(ViewerQuery()).execute()
        val viewerId = viewer.data?.Viewer?.userFragment?.id!!

        val sessionId = sessionDao.insertSession(LocalSession(null, accessToken, expiration, viewerId))
        dataStore.edit {
            it[SessionRepository.SESSION_KEY] = sessionId
        }
    }

    override fun initiateAuthFlow() {
        val baseUrl = "https://anilist.co/api/v2/oauth/authorize"
        val url = baseUrl.toHttpUrl().newBuilder()
            .addQueryParameter("client_id", "17834")
            .addQueryParameter("response_type", "token")
            .build()
            .toString()

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }
}