package com.sample.insta.ui.profile.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.sample.insta.R
import com.sample.insta.data.model.Image
import com.sample.insta.data.model.User
import com.sample.insta.data.remote.Networking
import com.sample.insta.data.remote.response.UserDetails
import com.sample.insta.data.repository.PhotoRepository
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
import javax.inject.Inject

class EditProfileViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    private val directory: File,
    private val photoRepository: PhotoRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {


    var name: MutableLiveData<String> = MutableLiveData()
    var profilePicUrl: MutableLiveData<Image> = MutableLiveData()
    var tagLine: MutableLiveData<String> = MutableLiveData()

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val localImagePath: MutableLiveData<String> = MutableLiveData()

    private val userData: User = userRepository.getCurrentUserDataFromPref()!!
    private val headers: Map<String, String> = mapOf(
        Pair(Networking.HEADER_ACCESS_TOKEN, userData.accessToken),
        Pair(Networking.HEADER_USER_ID, userData.userId),
        Pair(Networking.HEADER_API_KEY, Networking.API_KEY)
    )

    override fun onCreate() {

    }


    fun getDataFromBundle(user: UserDetails?) {
        name.postValue(user?.name)
        profilePicUrl.postValue(user?.profilePicUrl?.let { Image(it, headers) })
        tagLine.postValue(user?.tagLine)

    }

    fun onGalleryImageSelected(inputStream: InputStream) {
        loading.postValue(true)
        compositeDisposable.add(
            Single.fromCallable {
                FileUtils.saveInputStreamToFile(inputStream, directory, "gallery_img_temp", 500)
            }.subscribeOn(schedulerProvider.io()).subscribe(
                {
                    if (it != null) {
                        localImagePath.postValue(it.absolutePath)
                        uploadPhotoAndUpdateUserDetails(it)
                        FileUtils.getImageSize(it)?.run {
                            //        uploadPhotoAndCreatePost(it, this)
                            // uploadPhotoAndUpdateUserDetails(it)
                            //   profilePicUrl.postValue(Image(, headers))
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
                            localImagePath.postValue(this.absolutePath)
                            uploadPhotoAndUpdateUserDetails(this)
                            FileUtils.getImageSize(this)?.let { size ->

                            } ?: loading.postValue(false)
                        }
                    }, {
                        loading.postValue(false)
                        messageStringId.postValue(Resource.error(R.string.try_again))
                    }
                )
        )
    }


    private fun uploadPhotoAndUpdateUserDetails(imageFile: File) {
        val name = name.value!!
        val tagLine = tagLine.value!!

        compositeDisposable.add(
            photoRepository.uploadPhoto(
                imageFile, userData
            ).flatMap {
                userRepository.updateUser(name, tagLine, it, userData)

            }
                .subscribeOn(schedulerProvider.io()).subscribe(
                    {

                        loading.postValue(false)
                        //     post.postValue(Event(it))
                    }, {
                        handleNetworkError(it)
                        loading.postValue(false)
                    }
                )

        )

    }
}