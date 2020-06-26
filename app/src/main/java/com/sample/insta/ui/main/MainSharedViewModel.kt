package com.sample.insta.ui.main

import androidx.lifecycle.MutableLiveData
import com.sample.insta.data.model.Post
import com.sample.insta.ui.base.BaseViewModel
import com.sample.insta.utils.common.Event
import com.sample.insta.utils.network.NetworkHelper
import com.sample.insta.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class MainSharedViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    override fun onCreate() {

    }

    val homeRedirection = MutableLiveData<Event<Boolean>>()
    val newPost: MutableLiveData<Event<Post>> = MutableLiveData()

    fun onHomeRedirect() {
        homeRedirection.postValue(Event(true))
    }


}