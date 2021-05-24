package com.example.mygraphql.networking

import com.apollographql.apollo.ApolloClient
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class GraphQlApiTest {
    @Test
    fun `given two webservices then check for not equal`() {
        val mockApollo = mockk<ApolloClient>()

        val objectUnderTest = GraphQlApi.getApolloClient()

        Assert.assertNotEquals(objectUnderTest, mockApollo)
    }
}