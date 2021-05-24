package com.example.mygraphql.networking

import android.os.Looper
import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

object GraphQlApi {

    fun getApolloClient(): ApolloClient {
        check(Looper.myLooper() == Looper.getMainLooper()) {
            "Only the main thread can get the apolloClient instance"
        }

        val okHttpClient = OkHttpClient.Builder().build()
        return ApolloClient.builder()
            .serverUrl("https://graphqlzero.almansi.me/api")
            .okHttpClient(okHttpClient)
            .build()
    }
}