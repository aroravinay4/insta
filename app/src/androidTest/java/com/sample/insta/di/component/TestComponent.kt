package com.sample.insta.di.component

import com.sample.insta.di.components.ApplicationComponent
import com.sample.insta.di.module.ApplicationTestModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationTestModule::class])
interface TestComponent : ApplicationComponent {
}