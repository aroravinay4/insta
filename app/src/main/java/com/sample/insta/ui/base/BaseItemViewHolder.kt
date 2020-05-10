package com.sample.insta.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.sample.insta.application.InstaApplication
import com.sample.insta.di.components.DaggerViewHolderComponent
import com.sample.insta.di.components.ViewHolderComponent
import com.sample.insta.utils.display.Toaster
import javax.inject.Inject

abstract class BaseItemViewHolder<T : Any, VM : BaseItemViewModel<T>>(
    @LayoutRes layoutId: Int, parent: ViewGroup
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)),
    LifecycleOwner {


    init {
        onCreated()
    }

    @Inject
    lateinit var viewModel: VM

    @Inject
    lateinit var lifeCycleRegistry: LifecycleRegistry

    override fun getLifecycle(): Lifecycle = lifeCycleRegistry

    open fun bind(data: T) {
        viewModel.updateData(data)
    }

    private fun onCreated() {
        injectDependency(buildViewHolderComponent())
        lifeCycleRegistry.currentState = Lifecycle.State.INITIALIZED
        lifeCycleRegistry.currentState = Lifecycle.State.CREATED
        setupObservers()
        setupView(itemView)
    }

    fun onStart() {
        lifeCycleRegistry.currentState = Lifecycle.State.STARTED
        lifeCycleRegistry.currentState = Lifecycle.State.RESUMED
    }


    fun onDestroy() {
        lifeCycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    fun onStop() {
        lifeCycleRegistry.currentState = Lifecycle.State.STARTED
        lifeCycleRegistry.currentState = Lifecycle.State.CREATED
    }


    private fun buildViewHolderComponent() =
        DaggerViewHolderComponent.builder()
            .applicationComponent((itemView.context.applicationContext as InstaApplication).applicationComponent)
            .build()

    private fun showMessage(message: String) = Toaster.show(itemView.context, message)


    private fun showMessage(@StringRes resId: Int) = showMessage(itemView.context.getString(resId))


    protected open fun setupObservers() {
        viewModel.messageString.observe(this, Observer {
            it.data?.run { showMessage(this) }
        })

        viewModel.messageStringId.observe(this, Observer {
            it.data?.run { showMessage(this) }
        })
    }


    protected abstract fun injectDependency(viewHolderComponent: ViewHolderComponent)

    protected abstract fun setupView(view: View)

}