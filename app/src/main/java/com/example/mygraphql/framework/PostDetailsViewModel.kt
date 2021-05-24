package com.example.mygraphql.framework

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygraphql.PostQuery
import com.example.mygraphql.repository.GraphQlRepository
import com.example.mygraphql.view.state.ViewState
import kotlinx.coroutines.launch

class PostDetailsViewModel(val repository: GraphQlRepository) : ViewModel() {

    private val _post by lazy { MutableLiveData<ViewState<PostQuery.Data>>() }
    val post: LiveData<ViewState<PostQuery.Data>>
        get() = _post

    fun getPostDetails(id: String) = viewModelScope.launch {
        _post.postValue(ViewState.Loading)
        viewModelScope.launch {
            val response = repository.queryPostDetails(id)
            response.let { data ->
                when (data) {
                    is ViewState.Success -> {
                        _post.postValue(data)
                        Log.d("queryPost()", "response: $data")
                    }
                    is ViewState.Error -> {
                        _post.postValue(data)
                        Log.e("queryPost(id)", "error block")
                    }
                    else -> {
                        _post.postValue(data)
                        Log.e("queryPost()", "catch block")
                    }
                }
            }
        }
    }
}