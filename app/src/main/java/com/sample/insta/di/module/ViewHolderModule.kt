package com.sample.insta.di.module

import androidx.lifecycle.LifecycleRegistry
import com.sample.insta.di.scope.ViewHolderScope
import com.sample.insta.ui.base.BaseItemViewHolder
import com.sample.insta.ui.base.BaseViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewHolderModule(private val viewHolder: BaseItemViewHolder<*, *>) {

    @Provides
    @ViewHolderScope
    fun provideLifeCycleRegistry(): LifecycleRegistry = LifecycleRegistry(viewHolder)

}