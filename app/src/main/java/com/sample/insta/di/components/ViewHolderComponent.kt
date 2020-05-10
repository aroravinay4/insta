package com.sample.insta.di.components

import com.sample.insta.di.module.FragmentModule
import com.sample.insta.di.module.ViewHolderModule
import com.sample.insta.di.scope.FragmentScope
import com.sample.insta.di.scope.ViewHolderScope
import dagger.Component

@ViewHolderScope
@Component(dependencies = [ApplicationComponent::class], modules = [ViewHolderModule::class])
interface ViewHolderComponent {

}