package com.sample.insta.di.components

import com.sample.insta.di.module.FragmentModule
import com.sample.insta.di.scope.FragmentScope
import dagger.Component

@FragmentScope
@Component(dependencies = [ApplicationComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {

}