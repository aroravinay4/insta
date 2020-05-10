package com.sample.insta.ui.splash

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import com.sample.insta.data.repository.UserRepository
import com.sample.insta.ui.base.BaseViewModel
import com.sample.insta.utils.common.Event
import com.sample.insta.utils.network.NetworkHelper
import com.sample.insta.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable


class SplashViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val launchLogin: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
    val launchHomeActivity: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()


    override fun onCreate() {
        Handler().postDelayed({
            if (userRepository.getCurrentUserDataFromPref() != null)
                launchHomeActivity.postValue(Event(emptyMap()))
            else
                launchLogin.postValue(Event(emptyMap()))

        }, 2000)

    }


}

