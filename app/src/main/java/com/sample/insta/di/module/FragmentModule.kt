package com.sample.insta.di.module

import android.graphics.Camera
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.insta.data.repository.PhotoRepository
import com.sample.insta.data.repository.PostRepository
import com.sample.insta.data.repository.UserRepository
import com.sample.insta.di.qualifire.TempDirectory
import com.sample.insta.ui.base.BaseFragment
import com.sample.insta.ui.home.HomeFragmentViewModel
import com.sample.insta.ui.home.posts.PostAdapter
import com.sample.insta.ui.main.MainSharedViewModel
import com.sample.insta.ui.photo.PhotoViewModel
import com.sample.insta.ui.profile.ProfileViewModel
import com.sample.insta.utils.ViewModelProviderFactory
import com.sample.insta.utils.network.NetworkHelper
import com.sample.insta.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import java.io.File

@Module
class FragmentModule(private val fragment: BaseFragment<*>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(fragment.context)

    @Provides
    fun providePotsAdapter() = PostAdapter(fragment.lifecycle, ArrayList())


    @Provides
    fun provideHomeFragmentViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository,
        postRepository: PostRepository
    ): HomeFragmentViewModel =
        ViewModelProvider(fragment, ViewModelProviderFactory(HomeFragmentViewModel::class) {
            HomeFragmentViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper,
                userRepository,
                postRepository,
                ArrayList(),
                PublishProcessor.create()
            )
        }).get(HomeFragmentViewModel::class.java)


    @Provides
    fun provideProfileViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): ProfileViewModel =
        ViewModelProvider(fragment, ViewModelProviderFactory(ProfileViewModel::class) {
            ProfileViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper,
                userRepository
            )
        }).get(ProfileViewModel::class.java)

    @Provides
    fun providePhotoViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository,
        photoRepository: PhotoRepository,
        postRepository: PostRepository,
        @TempDirectory directory: File
    ): PhotoViewModel =
        ViewModelProvider(fragment, ViewModelProviderFactory(PhotoViewModel::class) {
            PhotoViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper,
                directory,
                userRepository,
                postRepository,
                photoRepository
            )
        }).get(PhotoViewModel::class.java)


    @Provides
    fun provideMainSharedViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
    ): MainSharedViewModel =
        ViewModelProvider(
            fragment.activity!!,
            ViewModelProviderFactory(MainSharedViewModel::class) {
                MainSharedViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper
                )
            }).get(MainSharedViewModel::class.java)


    @Provides
    fun provideCamera() = com.mindorks.paracamera.Camera.Builder().resetToCorrectOrientation(true)
        .setTakePhotoRequestCode(1).setDirectory("temp").setName("camera_temp_img")
        .setImageFormat(com.mindorks.paracamera.Camera.IMAGE_JPEG).setCompression(75)
        .setImageHeight(500).build(fragment)


}