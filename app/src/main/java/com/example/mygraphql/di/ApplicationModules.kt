package com.example.mygraphql.di

import com.example.mygraphql.framework.PostDetailsViewModel
import com.example.mygraphql.framework.PostListViewModel
import com.example.mygraphql.networking.GraphQlApi
import com.example.mygraphql.repository.GraphQlRepository
import com.example.mygraphql.repository.GraphQlRepositoryImpl
import com.example.mygraphql.view.ui.PostDetailsFragment
import com.example.mygraphql.view.ui.PostListFragment
import org.koin.dsl.module

val networkModule = module {
    factory { provideApolloClient() }
}

val repositoryModule = module {
    single<GraphQlRepository> { GraphQlRepositoryImpl(get()) }
}

val fragmentModule = module {
    factory { PostListFragment() }
    factory { PostDetailsFragment() }
}

val viewModelModule = module {
    factory { PostListViewModel(get()) }
    factory { PostDetailsViewModel(get()) }
}

fun provideApolloClient() = GraphQlApi.getApolloClient()