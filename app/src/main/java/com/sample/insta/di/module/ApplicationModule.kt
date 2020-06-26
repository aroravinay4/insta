package com.sample.insta.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.FileUtils
import com.sample.insta.BuildConfig
import com.sample.insta.application.InstaApplication
import com.sample.insta.data.remote.NetWorkService
import com.sample.insta.data.remote.Networking
import com.sample.insta.di.qualifire.ApplicationContext
import com.sample.insta.di.qualifire.TempDirectory
import com.sample.insta.utils.network.NetworkHelper
import com.sample.insta.utils.rx.RxSchedulerProvider
import com.sample.insta.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: InstaApplication) {


    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(): Context = application

    @Provides
    @Singleton
    @TempDirectory
    fun provideTemporaryDirectory() =
        com.sample.insta.utils.common.FileUtils.getDirectory(application, "temp")

    @Provides
    @Singleton
    fun provideNetworkService(): NetWorkService =
        Networking.create(
            BuildConfig.API_KEY,
            BuildConfig.BASE_URL,
            application.cacheDir,
            10 * 1024 * 1024
        )


    @Provides
    @Singleton
    fun providerNetworkHelper(): NetworkHelper =
        NetworkHelper(application)

    @Provides
    @Singleton
    fun provideSharedPreference(): SharedPreferences =
        application.getSharedPreferences("insta-project-prefs", Context.MODE_PRIVATE)


    @Provides
    fun provideSchedulers(): SchedulerProvider = RxSchedulerProvider()

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

}