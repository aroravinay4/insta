package com.sample.insta.di.components

import com.sample.insta.di.module.FragmentModule
import com.sample.insta.di.module.ViewHolderModule
import com.sample.insta.di.scope.FragmentScope
import com.sample.insta.di.scope.ViewHolderScope
import com.sample.insta.ui.home.posts.PostItemViewHolder
import com.sample.insta.ui.home.posts.PostItemViewModel
import dagger.Component

@ViewHolderScope
@Component(dependencies = [ApplicationComponent::class], modules = [ViewHolderModule::class])
interface ViewHolderComponent {


    fun inject(viewHolder: PostItemViewHolder)

}