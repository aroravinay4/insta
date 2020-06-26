package com.sample.insta.di.module

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.insta.data.repository.PhotoRepository
import com.sample.insta.data.repository.UserRepository
import com.sample.insta.di.qualifire.TempDirectory
import com.sample.insta.ui.base.BaseActivity
import com.sample.insta.ui.main.HomeViewModel
import com.sample.insta.ui.login.LoginViewModel
import com.sample.insta.ui.main.MainSharedViewModel
import com.sample.insta.ui.profile.editprofile.EditProfileViewModel
import com.sample.insta.ui.splash.SplashViewModel
import com.sample.insta.utils.ViewModelProviderFactory
import com.sample.insta.utils.network.NetworkHelper
import com.sample.insta.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import java.io.File


@Module
class ActivityModule(private val activity: BaseActivity<*>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(activity)


    @Provides
    fun provideSplashViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): SplashViewModel =
        ViewModelProvider(activity, ViewModelProviderFactory(SplashViewModel::class) {
            SplashViewModel(schedulerProvider, compositeDisposable, networkHelper, userRepository)
        }).get(SplashViewModel::class.java)

    @Provides
    fun provideLoginViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): LoginViewModel =
        ViewModelProvider(activity, ViewModelProviderFactory(LoginViewModel::class) {
            LoginViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper,
                userRepository
            )
        }).get(LoginViewModel::class.java)

    @Provides
    fun provideHomeViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
    ): HomeViewModel =
        ViewModelProvider(activity, ViewModelProviderFactory(HomeViewModel::class) {
            HomeViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper
            )
        }).get(HomeViewModel::class.java)


    @Provides
    fun provideMainSharedViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
    ): MainSharedViewModel =
        ViewModelProvider(activity, ViewModelProviderFactory(MainSharedViewModel::class) {
            MainSharedViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper
            )
        }).get(MainSharedViewModel::class.java)

    @Provides
    fun provideEditProfileViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository,
        photoRepository: PhotoRepository,
        @TempDirectory directory: File
    ): EditProfileViewModel =
        ViewModelProvider(activity, ViewModelProviderFactory(EditProfileViewModel::class) {
            EditProfileViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper,
                userRepository,
                directory,
                photoRepository

            )
        }).get(EditProfileViewModel::class.java)

    @Provides
    fun provideCamera() = com.mindorks.paracamera.Camera.Builder().resetToCorrectOrientation(true)
        .setTakePhotoRequestCode(1).setDirectory("temp").setName("camera_temp_img")
        .setImageFormat(com.mindorks.paracamera.Camera.IMAGE_JPEG).setCompression(75)
        .setImageHeight(500).build(activity)
}