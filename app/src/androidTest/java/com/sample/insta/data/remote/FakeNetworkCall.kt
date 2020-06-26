package com.sample.insta.data.remote

import com.sample.insta.data.model.Post
import com.sample.insta.data.remote.request.*
import com.sample.insta.data.remote.response.*
import io.reactivex.Single
import okhttp3.MultipartBody
import java.util.*

class FakeNetworkCall : NetWorkService {
    override fun doLoginCall(request: LoginRequest, apiKey: String): Single<LoginResponse> {
        TODO("Not yet implemented")
    }

    override fun doSignUp(request: SignUpRequest, apiKey: String): Single<LoginResponse> {
        TODO("Not yet implemented")
    }

    override fun getPostList(
        firstPostId: String?,
        lastPostId: String?,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<PostListResponse> {
        val creator1 = Post.User("userId", "name", "profilePicUrl")
        val creator2 = Post.User("userId2", "name2", "profilePicUrl2")

        val likedBy = mutableListOf<Post.User>(
            Post.User("userId3", "name3", "profilePicUrl3"),
            Post.User("userId4", "name4", "profilePicUrl4")
        )

        val post1 = Post("post1", "imgUrl1", 400, 400, creator1, likedBy, Date())
        val post2 = Post("post2", "imgUrl2", 400, 400, creator2, likedBy, Date())

        val postListResponse = PostListResponse("statusCode", "success", listOf(post1, post2))

        return Single.just(postListResponse)


    }

    override fun doLike(
        request: PostLikeRequest,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<GeneralResponse> {
        TODO("Not yet implemented")
    }

    override fun doUnLike(
        request: PostLikeRequest,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<GeneralResponse> {
        TODO("Not yet implemented")
    }

    override fun imageUpload(
        image: MultipartBody.Part,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<ImageResponse> {
        TODO("Not yet implemented")
    }

    override fun createPost(
        request: PostCreationRequest,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<PostCreationResponse> {
        TODO("Not yet implemented")
    }

    override fun getUserDetails(
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<UserInfoResponse> {
        TODO("Not yet implemented")
    }

    override fun logout(
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<GeneralResponse> {
        TODO("Not yet implemented")
    }

    override fun updateUserDetails(
        request: UpdateUserRequest,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<GeneralResponse> {
        TODO("Not yet implemented")
    }
}