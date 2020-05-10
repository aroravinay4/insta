package com.sample.insta.ui.home

import com.sample.insta.ui.base.BaseViewModel
import com.sample.insta.utils.network.NetworkHelper
import com.sample.insta.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class HomeViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    override fun onCreate() {

    }
}