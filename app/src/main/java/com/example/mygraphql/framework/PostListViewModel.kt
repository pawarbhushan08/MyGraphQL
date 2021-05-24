package com.example.mygraphql.framework

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygraphql.PostListQuery
import com.example.mygraphql.repository.GraphQlRepository
import com.example.mygraphql.view.state.ViewState
import kotlinx.coroutines.launch

class PostListViewModel(val repository: GraphQlRepository) : ViewModel() {

    private val _postList by lazy { MutableLiveData<ViewState<PostListQuery.Data>>() }

    val postList: LiveData<ViewState<PostListQuery.Data>>
        get() = _postList

    fun getPosts() = viewModelScope.launch {
        _postList.postValue(ViewState.Loading)
        viewModelScope.launch {
            val response = repository.queryPostList()
            response.let { data ->
                when (data) {
                    is ViewState.Success -> {
                        _postList.postValue(data)
                        Log.d("queryPostList()", "response: $data")
                    }
                    is ViewState.Error -> {
                        _postList.postValue(data)
                        Log.e("queryPost(id)", "error block")
                    }
                    else -> {
                        _postList.postValue(data)
                        Log.e("queryPostList()", "catch block")
                    }
                }
            }
        }
    }

}