package com.sample.insta.di.components

import com.sample.insta.ui.home.HomeActivity
import com.sample.insta.di.module.ActivityModule
import com.sample.insta.di.scope.ActivityScope
import com.sample.insta.ui.login.LoginActivity
import com.sample.insta.ui.signup.SignUpActivity
import com.sample.insta.ui.splash.SplashScreen
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(activity: SplashScreen)

    fun inject(activity: LoginActivity)

    fun inject(activity: SignUpActivity)

    fun inject(activity: HomeActivity)


}