package com.example.mygraphql.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.Logger
import com.example.mygraphql.PostListQuery
import com.example.mygraphql.helper.Utils
import com.example.mygraphql.repository.BaseRepository.Companion.SOMETHING_WRONG
import com.example.mygraphql.view.state.ViewState
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class GraphQlRepositoryImplTest {
    @RelaxedMockK
    private lateinit var mockData: PostListQuery.Data

    private val mockWebServer = MockWebServer()

    private lateinit var apolloClient: ApolloClient

    private lateinit var mockRepositoryImpl: GraphQlRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockWebServer.start(8080)

        val okHttpClient = OkHttpClient.Builder()
            .dispatcher(Dispatcher(Utils.immediateExecutorService()))
            .build()

        apolloClient = ApolloClient.builder()
            .serverUrl(mockWebServer.url("/"))
            .dispatcher(Utils.immediateExecutor())
            .okHttpClient(okHttpClient)
            .logger(object : Logger {
                override fun log(priority: Int, message: String, t: Throwable?, vararg args: Any) {
                    println(String.format(message, *args))
                    t?.printStackTrace()
                }
            }).build()

        mockRepositoryImpl = GraphQlRepositoryImpl(apolloClient)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `given response failure of list when fetching results then return exception`() {
        val expectedError = ViewState.Error(Exception(SOMETHING_WRONG))

        runBlocking {
            val actualResult = mockRepositoryImpl.queryPostList()

            assertEquals(expectedError::class.java, actualResult!!::class.java)
        }
    }

    @Test
    fun `given response posts list when fetching results then return success`() {
        mockWebServer.enqueue(Utils.mockResponse("post_list_response.json"))

        val expectedSuccess = ViewState.Success(mockData)

        runBlocking {
            val actualResult = mockRepositoryImpl.queryPostList()

            assertEquals(expectedSuccess::class.java, actualResult!!::class.java)
        }
    }

    @Test
    fun `given response failure for specific post when fetching results then return exception`() {
        val expectedError = ViewState.Error(Exception(SOMETHING_WRONG))

        runBlocking {
            val actualResult = mockRepositoryImpl.queryPostDetails("11")

            assertEquals(expectedError::class.java, actualResult!!::class.java)
        }
    }

    @Test
    fun `given response specific post when fetching results then return success`() {
        mockWebServer.enqueue(Utils.mockResponse("post_details_response.json"))

        val expectedSuccess = ViewState.Success(mockData)

        runBlocking {
            val actualResult = mockRepositoryImpl.queryPostDetails("1")

            assertEquals(expectedSuccess::class.java, actualResult!!::class.java)
        }
    }
}