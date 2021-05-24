package com.example.mygraphql.framework

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.apollographql.apollo.exception.ApolloException
import com.example.mygraphql.MainCoroutineRule
import com.example.mygraphql.PostListQuery
import com.example.mygraphql.repository.GraphQlRepository
import com.example.mygraphql.view.state.ViewState
import io.mockk.mockk
import junit.framework.Assert
import junit.framework.Assert.assertEquals
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
class PostListViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var mockRepository: GraphQlRepository

    @Mock
    private lateinit var postsListObserver: Observer<ViewState<PostListQuery.Data>>


    private lateinit var objectUnderTest: PostListViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        objectUnderTest = PostListViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        objectUnderTest.postList.removeObserver(postsListObserver)
    }

    @Test
    fun `when calling for posts list then return loading`() {
        testCoroutineRule.runBlockingTest {
            objectUnderTest.postList.observeForever(postsListObserver)

            objectUnderTest.getPosts()

            Mockito.verify(postsListObserver).onChanged(ViewState.Loading)
        }
    }

    @Test
    fun `when fetching for posts list fails then return an error`() {
        val exception = mockk<ApolloException>()
        testCoroutineRule.runBlockingTest {
            objectUnderTest.postList.observeForever(postsListObserver)

            Mockito.`when`(mockRepository.queryPostList()).thenAnswer {
                ViewState.Error(exception)
            }

            objectUnderTest.getPosts()

            Assert.assertNotNull(objectUnderTest.postList.value)
            assertEquals(ViewState.Error(exception), objectUnderTest.postList.value)
        }
    }

    @Test
    fun `when fetching for posts list then return a list success state`() {
        val mockPosts = mockk<PostListQuery.Data>()

        testCoroutineRule.runBlockingTest {
            objectUnderTest.postList.observeForever(postsListObserver)

            Mockito.`when`(mockRepository.queryPostList()).thenAnswer {
                ViewState.Success(mockPosts)
            }

            objectUnderTest.getPosts()

            Assert.assertNotNull(objectUnderTest.postList.value)
            assertEquals(ViewState.Success(mockPosts), objectUnderTest.postList.value)
        }
    }

}