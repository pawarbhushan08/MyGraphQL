package com.example.mygraphql.repository

import com.example.mygraphql.PostListQuery
import com.example.mygraphql.PostQuery
import com.example.mygraphql.view.state.ViewState

interface GraphQlRepository {

    suspend fun queryPostList(): ViewState<PostListQuery.Data>?

    suspend fun queryPostDetails(id: String): ViewState<PostQuery.Data>?
}