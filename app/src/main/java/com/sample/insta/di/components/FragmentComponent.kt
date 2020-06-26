package com.sample.insta.di.components

import com.sample.insta.di.module.FragmentModule
import com.sample.insta.di.scope.FragmentScope
import com.sample.insta.ui.home.HomeFragment
import com.sample.insta.ui.photo.PhotoFragment
import com.sample.insta.ui.profile.ProfileFragment
import dagger.Component

@FragmentScope
@Component(dependencies = [ApplicationComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(fragment: HomeFragment)
    fun inject(fragment: PhotoFragment)
    fun inject(fragment: ProfileFragment)


}