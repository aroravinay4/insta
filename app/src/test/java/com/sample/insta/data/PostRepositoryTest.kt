package com.sample.insta.data

import com.sample.insta.data.model.User
import com.sample.insta.data.remote.NetWorkService
import com.sample.insta.data.remote.Networking
import com.sample.insta.data.remote.response.PostListResponse
import com.sample.insta.data.repository.PostRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class PostRepositoryTest {


    @Mock
    private lateinit var networkService: NetWorkService

    private lateinit var postRepository: PostRepository

    @Before
    fun setUp() {
        Networking.API_KEY = "FAKE_API_KEY"
        postRepository = PostRepository(networkService)
    }

    @Test
    fun fetchHomePostList_requestDoHomePostListCall() {

        val user = User("userId", "userName", "userEmail", "accessToken", "profilePicUrl")

        doReturn(Single.just(PostListResponse("statusCode", "message", listOf())))
            .`when`(networkService)
            .getPostList(
                "firstPostId",
                "lastPostId",
                user.userId,
                user.accessToken,
                Networking.API_KEY
            )

        postRepository.fetchPostData("firstPostId", "lastPostId", user)

        verify(networkService).getPostList(
            "firstPostId",
            "lastPostId",
            user.userId,
            user.accessToken,
            Networking.API_KEY
        )

    }
}