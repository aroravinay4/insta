package com.sample.insta.di.module

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.insta.data.repository.UserRepository
import com.sample.insta.ui.base.BaseActivity
import com.sample.insta.ui.home.HomeViewModel
import com.sample.insta.ui.login.LoginViewModel
import com.sample.insta.ui.splash.SplashViewModel
import com.sample.insta.utils.ViewModelProviderFactory
import com.sample.insta.utils.network.NetworkHelper
import com.sample.insta.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable


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
        ViewModelProvider(ViewModelStore(), ViewModelProviderFactory(SplashViewModel::class) {
            SplashViewModel(schedulerProvider, compositeDisposable, networkHelper, userRepository)
        }).get(SplashViewModel::class.java)

    @Provides
    fun provideLoginViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): LoginViewModel =
        ViewModelProvider(ViewModelStore(), ViewModelProviderFactory(LoginViewModel::class) {
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
        ViewModelProvider(ViewModelStore(), ViewModelProviderFactory(HomeViewModel::class) {
            HomeViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper
            )
        }).get(HomeViewModel::class.java)



}