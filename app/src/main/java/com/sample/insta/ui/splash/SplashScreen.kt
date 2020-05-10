package com.sample.insta.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.sample.insta.R
import com.sample.insta.di.components.ActivityComponent
import com.sample.insta.ui.base.BaseActivity
import com.sample.insta.ui.home.HomeActivity
import com.sample.insta.ui.login.LoginActivity

class SplashScreen : BaseActivity<SplashViewModel>() {
    override fun provideLayoutId(): Int = R.layout.activity_splash_screen

    override fun injectDependency(activity: ActivityComponent) = activity.inject(this)

    override fun setUpView(savedInstanceState: Bundle?) {}

    override fun setupObservers() {
        super.setupObservers()

        viewModel.launchLogin.observe(this, Observer {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
        })

        viewModel.launchHomeActivity.observe(this, Observer {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()
            }
        })
    }


}
