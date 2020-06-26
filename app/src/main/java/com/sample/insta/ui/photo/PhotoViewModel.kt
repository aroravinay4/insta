package com.sample.insta.ui.photo

import androidx.lifecycle.MutableLiveData
import com.sample.insta.R
import com.sample.insta.data.model.Post
import com.sample.insta.data.model.User
import com.sample.insta.data.repository.PhotoRepository
import com.sample.insta.data.repository.PostRepository
import com.sample.insta.data.repository.UserRepository
import com.sample.insta.ui.base.BaseViewModel
import com.sample.insta.utils.common.Event
import com.sample.insta.utils.common.FileUtils
import com.sample.insta.utils.common.Resource
import com.sample.insta.utils.network.NetworkHelper
import com.sample.insta.utils.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.io.InputStream

class PhotoViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val directory: File,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val photoRepository: PhotoRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val post: MutableLiveData<Event<Post>> = MutableLiveData()

    private val user: User = userRepository.getCurrentUserDataFromPref()!!


    override fun onCreate() {

    }


    fun onGalleryImageSelected(inputStream: InputStream) {
        loading.postValue(true)
        compositeDisposable.add(
            Single.fromCallable {
                FileUtils.saveInputStreamToFile(inputStream, directory, "gallery_img_temp", 500)
            }.subscribeOn(schedulerProvider.io()).subscribe(
                {
                    if (it != null) {
                        FileUtils.getImageSize(it)?.run {
                            uploadPhotoAndCreatePost(it, this)
                        }
                    } else {
                        loading.postValue(false)
                        messageStringId.postValue(
                            Resource.error(
                                R.string.try_again
                            )
                        )
                    }
                }, {
                    loading.postValue(false)
                    messageStringId.postValue(Resource.error(R.string.try_again))
                }
            )
        )

    }


    fun onCameraImageTaken(cameraImageProcessor: () -> String) {
        loading.postValue(true)
        compositeDisposable.add(
            Single.fromCallable { cameraImageProcessor() }.subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                        File(it).apply {
                            FileUtils.getImageSize(this)?.let { size ->
                                uploadPhotoAndCreatePost(this, size)
                            } ?: loading.postValue(false)
                        }
                    }, {
                        loading.postValue(false)
                        messageStringId.postValue(Resource.error(R.string.try_again))
                    }
                )
        )
    }


    private fun uploadPhotoAndCreatePost(imageFile: File, imageSize: Pair<Int, Int>) {
        compositeDisposable.add(
            photoRepository.uploadPhoto(
                imageFile, user
            ).flatMap {
                postRepository.createPost(it, imageSize.first, imageSize.second, user)
            }
                .subscribeOn(schedulerProvider.io()).subscribe(
                    {
                        loading.postValue(false)
                        post.postValue(Event(it))
                    }, {

                        handleNetworkError(it)
                        loading.postValue(false)
                    }
                )

        )

    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}