package com.sample.insta.ui.home

import android.os.Bundle
import com.sample.insta.R
import com.sample.insta.di.components.ActivityComponent
import com.sample.insta.ui.base.BaseActivity

class HomeActivity : BaseActivity<HomeViewModel>() {
    override fun provideLayoutId(): Int = R.layout.activity_main

    override fun injectDependency(activity: ActivityComponent) = activity.inject(this)

    override fun setUpView(savedInstanceState: Bundle?) {

    }




}
