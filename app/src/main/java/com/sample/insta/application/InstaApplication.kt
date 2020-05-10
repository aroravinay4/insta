package com.sample.insta.application

import android.app.Application
import com.sample.insta.di.components.ApplicationComponent
import com.sample.insta.di.components.DaggerApplicationComponent
import com.sample.insta.di.module.ApplicationModule
import dagger.Component

class InstaApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent


    override fun onCreate() {
        super.onCreate()
        injectDependency()
    }


    private fun injectDependency() {
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(
            ApplicationModule(this)
        ).build()

        applicationComponent.inject(this)

    }

    fun setComponent(applicationComponent: ApplicationComponent) {
        this.applicationComponent = applicationComponent
    }
}