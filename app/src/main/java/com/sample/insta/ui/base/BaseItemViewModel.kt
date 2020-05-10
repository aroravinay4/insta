package com.sample.insta.ui.base

import androidx.lifecycle.MutableLiveData
import com.sample.insta.utils.network.NetworkHelper
import com.sample.insta.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

abstract class BaseItemViewModel<T : Any>(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val data = MutableLiveData<T>()


    fun updateData(data: T) {
        this.data.postValue(data)
    }


    fun onManualCleared() = onCleared()

}