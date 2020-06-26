package com.sample.insta.ui.profile

import android.util.EventLog
import androidx.lifecycle.MutableLiveData
import com.sample.insta.data.model.Image
import com.sample.insta.data.model.User
import com.sample.insta.data.remote.response.UserDetails
import com.sample.insta.data.repository.UserRepository
import com.sample.insta.ui.base.BaseViewModel
import com.sample.insta.utils.common.Event
import com.sample.insta.utils.network.NetworkHelper
import com.sample.insta.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ProfileViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {


     val user: User = userRepository.getCurrentUserDataFromPref()!!
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val name: MutableLiveData<String> = MutableLiveData()
    val profilePicUrl: MutableLiveData<String> = MutableLiveData()
    val tagLine: MutableLiveData<String> = MutableLiveData()
    val launchLoginActivity: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
    val launchEditProfileActivity: MutableLiveData<Event<UserDetails>> = MutableLiveData()

    override fun onCreate() {

    }


    fun getDetails() {
        loading.postValue(true)
        compositeDisposable.add(
            userRepository.getUsersInfo(user).subscribeOn(schedulerProvider.io()).subscribe(
                {
                    it?.let {
                        userRepository.updateUserData(it)
                        name.postValue(it.name)
                        profilePicUrl.postValue(it.profilePicUrl)
                        tagLine.postValue(it.tagLine)
                        loading.postValue(false)
                        launchEditProfileActivity.postValue(Event(it))
                    }


                }, {
                    handleNetworkError(it)
                    loading.postValue(false)

                }
            )
        )
    }

    fun logout() {
        loading.postValue(true)
        compositeDisposable.add(
            userRepository.logout(user).subscribeOn(schedulerProvider.io()).subscribe(
                {
                    loading.postValue(false)
                    userRepository.removeUser()
                    launchLoginActivity.postValue(Event(emptyMap()))

                }, {
                    handleNetworkError(it)
                    loading.postValue(false)

                }
            )
        )
    }

}