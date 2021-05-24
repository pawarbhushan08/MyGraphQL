package com.example.mygraphql.repository

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.mygraphql.PostListQuery
import com.example.mygraphql.PostQuery
import com.example.mygraphql.networking.GraphQlApi
import com.example.mygraphql.view.state.ViewState

class GraphQlRepositoryImpl(private val webService: ApolloClient): BaseRepository() {

    override suspend fun queryPostList(): ViewState<PostListQuery.Data>? {
        var result: ViewState<PostListQuery.Data>? = null
        try {
            val response = webService.query(PostListQuery()).await()
            response.let {
                it.data?.let { data -> result = handleSuccess(data) }
            }
        } catch (e: ApolloException) {
            Log.e("GraphQlRepo", "Error: ${e.message}")
            return handleException(GENERAL_ERROR_CODE)
        }
        return result
    }

    override suspend fun queryPostDetails(id: String): ViewState<PostQuery.Data>? {
        var result: ViewState<PostQuery.Data>? = null
        try {
            val response = webService.query(PostQuery(id)).await()
            response.let {
                it.data?.let { data -> result = handleSuccess(data) }
            }
        } catch (ae: ApolloException) {
            Log.e("GraphQlRepo", "Error: ${ae.message}")
            return handleException(GENERAL_ERROR_CODE)
        }
        return result
    }

}