package com.sample.insta.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sample.insta.application.InstaApplication
import com.sample.insta.di.components.ActivityComponent
import com.sample.insta.di.components.DaggerActivityComponent
import com.sample.insta.di.module.ActivityModule
import com.sample.insta.utils.display.Toaster
import javax.inject.Inject

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    @Inject
    lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependency(buildActivityComponent())
        super.onCreate(savedInstanceState)
        setContentView(provideLayoutId())
        setupObservers()
        setUpView(savedInstanceState)
        viewModel.onCreate()


    }

    private fun buildActivityComponent() =
        DaggerActivityComponent.builder()
            .applicationComponent((application as InstaApplication).applicationComponent)
            .activityModule(
                ActivityModule(this)
            ).build()


    protected open fun setupObservers() {
        viewModel.messageStringId.observe(this, Observer {
            it.data?.run { showMessage(this) }
        })


        viewModel.messageString.observe(this, Observer {
            it.data?.run { showMessage(this) }
        })
    }

    open fun goBack() = onBackPressed()

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStackImmediate()
        else super.onBackPressed()
    }

    private fun showMessage(message: String) = Toaster.show(applicationContext, message)

    private fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))


    @LayoutRes
    protected abstract fun provideLayoutId(): Int

    protected abstract fun injectDependency(activity: ActivityComponent)

    protected abstract fun setUpView(savedInstanceState: Bundle?)

}