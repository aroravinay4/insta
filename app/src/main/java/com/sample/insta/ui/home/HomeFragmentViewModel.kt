package com.sample.insta.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.insta.data.model.Post
import com.sample.insta.data.model.User
import com.sample.insta.data.repository.PostRepository
import com.sample.insta.data.repository.UserRepository
import com.sample.insta.ui.base.BaseViewModel
import com.sample.insta.utils.common.Resource
import com.sample.insta.utils.network.NetworkHelper
import com.sample.insta.utils.rx.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers

class HomeFragmentViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val allPostList: ArrayList<Post>,
    private val paginator: PublishProcessor<Pair<String?, String?>>
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val posts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()
    val reFreshPost: MutableLiveData<Resource<List<Post>>> = MutableLiveData()
    private val user: User = userRepository.getCurrentUserDataFromPref()!!

    var firstId: String? = null
    var lastId: String? = null

    init {
        compositeDisposable.add(
            paginator
                .onBackpressureDrop().doOnNext {
                    loading.postValue(true)
                }.concatMapSingle { pageId ->
                    return@concatMapSingle postRepository.fetchPostData(
                        pageId.first,
                        pageId.second,
                        user
                    ).subscribeOn(schedulerProvider.io()).doOnError {
                        handleNetworkError(it)
                    }

                }.subscribe(
                    {
                        allPostList.addAll(it)

                        firstId = allPostList.maxBy { post -> post.createdAt.time }?.id
                        lastId = allPostList.minBy { post -> post.createdAt.time }?.id

                        loading.postValue(false)
                        posts.postValue(Resource.success(it))

                    }, {
                        handleNetworkError(it)
                    }
                )

        )
    }


    override fun onCreate() {
        loadMorePosts()
    }

    private fun loadMorePosts() {
/*        val firstPostId = if (allPostList.isNotEmpty()) allPostList[0].id else null
        val lastPostId = if (allPostList.size > 1) allPostList[allPostList.size - 1].id else null*/

        if (checkInternetConnectionWithMessage())
            paginator.onNext(Pair(firstId, lastId))
    }

    fun onLoadMore() {
        if (loading.value !== null && loading.value == false)
            loadMorePosts()
    }

    fun onNewPost(post: Post) {
        allPostList.add(0, post)
        reFreshPost.postValue(Resource.success(mutableListOf<Post>().apply { addAll(allPostList) }))
    }
}