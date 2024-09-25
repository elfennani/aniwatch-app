package com.elfennani.aniwatch.data.remote.converters

import com.apollographql.apollo.ApolloClient
import com.elfennani.BasicShowQuery
import com.elfennani.type.MediaListStatus

suspend fun fetchShow() {
    val apolloClient = ApolloClient.Builder()
        .serverUrl("https://graphql.anilist.co")
        .build()

    val response =
        apolloClient.query(BasicShowQuery(123, MediaListStatus.PAUSED, listOf())).execute()
    response.data?.collection?.lists?.forEach {
        it?.entries?.forEach { it?.media?.showFragment }
    }

}