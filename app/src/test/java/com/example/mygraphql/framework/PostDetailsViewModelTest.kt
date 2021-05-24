package com.example.mygraphql.framework

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.apollographql.apollo.exception.ApolloException
import com.example.mygraphql.MainCoroutineRule
import com.example.mygraphql.PostQuery
import com.example.mygraphql.repository.GraphQlRepository
import com.example.mygraphql.view.state.ViewState
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class PostDetailsViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var mockRepository: GraphQlRepository

    @Mock
    private lateinit var postObserver: Observer<ViewState<PostQuery.Data>>

    private lateinit var objectUnderTest: PostDetailsViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        objectUnderTest = PostDetailsViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        objectUnderTest.post.removeObserver(postObserver)
    }
    @Test
    fun `when calling for a specific post then return loading`() {
        testCoroutineRule.runBlockingTest {
            objectUnderTest.post.observeForever(postObserver)

            objectUnderTest.getPostDetails("10")

            Mockito.verify(postObserver).onChanged(ViewState.Loading)
        }
    }

    @Test
    fun `when calling for a specific post fails then return an error`() {
        val exception = mockk<ApolloException>()
        testCoroutineRule.runBlockingTest {
            objectUnderTest.post.observeForever(postObserver)

            Mockito.`when`(mockRepository.queryPostDetails("255")).thenAnswer {
                ViewState.Error(exception)
            }

            objectUnderTest.getPostDetails("255")

            assertNotNull(objectUnderTest.post.value)
            assertEquals(ViewState.Error(exception), objectUnderTest.post.value)
        }
    }


    @Test
    fun `when fetching for a specific post then return a list success state`() {
        val mockPost = mockk<PostQuery.Data>()

        testCoroutineRule.runBlockingTest {
            objectUnderTest.post.observeForever(postObserver)

            Mockito.`when`(mockRepository.queryPostDetails("11")).thenAnswer {
                ViewState.Success(mockPost)
            }

            objectUnderTest.getPostDetails("11")

            assertNotNull(objectUnderTest.post.value)
            assertEquals(ViewState.Success(mockPost), objectUnderTest.post.value)
        }
    }
}