package com.sample.insta.di.components

import android.app.Application
import android.content.Context
import com.sample.insta.application.InstaApplication
import com.sample.insta.data.remote.NetWorkService
import com.sample.insta.data.repository.UserRepository
import com.sample.insta.di.module.ApplicationModule
import com.sample.insta.di.qualifire.ApplicationContext
import com.sample.insta.di.qualifire.TempDirectory
import com.sample.insta.utils.network.NetworkHelper
import com.sample.insta.utils.rx.SchedulerProvider
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(app: InstaApplication)

    @ApplicationContext
    fun getContext(): Context


    fun getApplication(): Application

    fun getNetworkService(): NetWorkService
    fun getNetworkHelper(): NetworkHelper

    fun getSchedulers(): SchedulerProvider
    fun getCompositeDisposable(): CompositeDisposable

    fun getUserRepository(): UserRepository

    @TempDirectory
    fun getTempDirectory(): File

}