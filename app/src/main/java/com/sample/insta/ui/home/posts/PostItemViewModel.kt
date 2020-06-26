package com.sample.insta.ui.home.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.sample.insta.R
import com.sample.insta.data.model.Image
import com.sample.insta.data.model.Post
import com.sample.insta.data.model.User
import com.sample.insta.data.remote.Networking
import com.sample.insta.data.repository.PostRepository
import com.sample.insta.data.repository.UserRepository
import com.sample.insta.ui.base.BaseItemViewModel
import com.sample.insta.utils.common.Resource
import com.sample.insta.utils.common.TimeUtils
import com.sample.insta.utils.display.ScreenUtils
import com.sample.insta.utils.network.NetworkHelper
import com.sample.insta.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostItemViewModel @Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper, userRepository: UserRepository,
    val postRepository: PostRepository
) : BaseItemViewModel<Post>(schedulerProvider, compositeDisposable, networkHelper) {

    companion object {
        const val TAG = "PostItemViewModel"
    }

    private val user: User = userRepository.getCurrentUserDataFromPref()!!
    private val screenHeight = ScreenUtils.getScreenHeight()
    private val screenWidth = ScreenUtils.getScreenWidth()

    private val headers: Map<String, String> = mapOf(
        Pair(Networking.HEADER_ACCESS_TOKEN, user.accessToken),
        Pair(Networking.HEADER_USER_ID, user.userId),
        Pair(Networking.HEADER_API_KEY, Networking.API_KEY)
    )

    val name: LiveData<String> = Transformations.map(data) { it.creator.name }
    val postTime: LiveData<String> =
        Transformations.map(data) { TimeUtils.getTimeAgo(it.createdAt) }
    val likesCount: LiveData<Int> = Transformations.map(data) { it.likedBy?.size ?: 0 }
    val isLiked: LiveData<Boolean> = Transformations.map(data) {
        it.likedBy?.find { postUser -> postUser.id == user.userId } != null
    }
    val profileImage: LiveData<Image> = Transformations.map(data) {
        it.creator.profilePicUrl?.run { Image(this, headers) }
    }

    val imageDetail: LiveData<Image> = Transformations.map(data) {
        Image(
            it.imageUrl,
            headers,
            screenWidth,
            it.imageHeight?.let { height ->
                return@let (calculateScaleFactor(it) * height).toInt()
            } ?: screenHeight / 3)
    }

    private fun calculateScaleFactor(post: Post) =
        post.imageWidth?.let { return@let screenWidth.toFloat() / it } ?: 1f

    override fun onCreate() {}

    fun onLikeClick() = data.value?.let {
        if (networkHelper.isNetworkConnected()) {
            val api =
                if (isLiked.value == true) {
                    postRepository.makeUnLikePost(it, user)
                } else
                    postRepository.makeLikePost(it, user)

            compositeDisposable.add(api.subscribeOn(Schedulers.io()).subscribe(
                { responsePost ->
                    if (responsePost.id == it.id)
                        updateData(responsePost)
                }, { error ->
                    handleNetworkError(error)

                }
            ))

        } else {
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
        }

    }


}