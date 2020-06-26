package com.sample.insta.data.repository

import com.sample.insta.data.model.Post
import com.sample.insta.data.model.User
import com.sample.insta.data.remote.NetWorkService
import com.sample.insta.data.remote.request.PostCreationRequest
import com.sample.insta.data.remote.request.PostLikeRequest
import io.reactivex.Single
import javax.inject.Inject

class PostRepository @Inject constructor(private val netWorkService: NetWorkService) {


    fun fetchPostData(
        firstPostId: String?,
        lastPostId: String?,
        user: User
    ): Single<List<Post>> {
        return netWorkService.getPostList(
            firstPostId,
            lastPostId,
            user.userId,
            user.accessToken
        ).map { it.data }
    }


    fun makeLikePost(post: Post, user: User): Single<Post> {
        return netWorkService.doLike(
            PostLikeRequest(postId = post.id),
            user.userId,
            user.accessToken
        ).map {
            post.likedBy?.apply {
                this.find { postUser -> postUser.id == user.userId } ?: this.add(
                    Post.User(
                        user.userId,
                        user.userName,
                        user.profilePicUrl
                    )
                )
            }
            return@map post
        }
    }

    fun makeUnLikePost(post: Post, user: User): Single<Post> {
        return netWorkService.doUnLike(
            PostLikeRequest(postId = post.id),
            user.userId,
            user.accessToken
        ).map {
            post.likedBy?.apply {
                this.find { postUser -> postUser.id == user.userId }?.let { this.remove(it) }
            }
            return@map post
        }
    }


    fun createPost(imgUrl: String, imgWidth: Int, imgHeight: Int, user: User): Single<Post> =
        netWorkService.createPost(
            PostCreationRequest(imgUrl, imgWidth, imgHeight), user.userId, user.accessToken
        ).map {
            Post(
                it.data.id,
                it.data.imageUrl,
                it.data.imageWidth,
                it.data.imageHeight,
                Post.User(
                    user.userId,
                    user.userName,
                    user.profilePicUrl
                ),
                mutableListOf(),
                it.data.createdAt
            )
        }

}