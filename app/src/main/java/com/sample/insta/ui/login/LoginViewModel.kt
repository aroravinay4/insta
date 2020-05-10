package com.sample.insta.ui.login

import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.sample.insta.data.repository.UserRepository
import com.sample.insta.ui.base.BaseViewModel
import com.sample.insta.utils.common.*
import com.sample.insta.utils.network.NetworkHelper
import com.sample.insta.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io

class LoginViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable, networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    private val validationList: MutableLiveData<List<Validation>> = MutableLiveData()

    val launchHomeActivity: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    val emailField: MutableLiveData<String> = MutableLiveData()
    val passwordField: MutableLiveData<String> = MutableLiveData()

    val nameField: MutableLiveData<String> = MutableLiveData()

    val logIn: MutableLiveData<Boolean> = MutableLiveData()

    val emailValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.EMAIL)
    val passwordValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.PASSWORD)

    val nameValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.NAME)


    private fun filterValidation(field: Validation.Field) =
        Transformations.map(validationList) {
            it.find { validation -> validation.field == field }?.run { return@run this.resource }
                ?: Resource.unknown()
        }


    override fun onCreate() {}

    fun onEmailChange(email: String) = emailField.postValue(email)
    fun onPasswordChange(password: String) = passwordField.postValue(password)

    fun onNameChange(name: String) = nameField.postValue(name)


    fun login() {
        val email = emailField.value
        val password = passwordField.value

        val validations = Validator.validateLoginFields(email, password, "", false)
        validationList.postValue(validations)

        if (validations.isNotEmpty() && email != null && password != null) {
            val successValidation = validations.filter { it.resource.status == Status.SUCCESS }

            if (successValidation.size == validations.size && checkInternetConnectionWithMessage()) {
                logIn.postValue(true)
                compositeDisposable.addAll(
                    userRepository.doLogin(email, password).subscribeOn(schedulerProvider.io())
                        .subscribe({
                            userRepository.saveUserData(it)
                            logIn.postValue(false)
                            launchHomeActivity.postValue(Event(emptyMap()))
                        },
                            {
                                handleNetworkError(it)
                                logIn.postValue(false)
                            }
                        )
                )
            }
        }


    }

    fun signUp() {
        val email = emailField.value
        val password = passwordField.value
        val name = nameField.value

        val validations = Validator.validateLoginFields(email, password, name, true)
        validationList.postValue(validations)

        if (validations.isNotEmpty() && email != null && password != null && name != null) {
            val successValidation = validations.filter { it.resource.status == Status.SUCCESS }

            if (successValidation.size == validations.size && checkInternetConnectionWithMessage()) {
                logIn.postValue(true)
                compositeDisposable.addAll(
                    userRepository.doSignUp(email, password, name)
                        .subscribeOn(schedulerProvider.io()).subscribe(
                            {
                                userRepository.saveUserData(it)
                                logIn.postValue(false)
                                launchHomeActivity.postValue(Event(emptyMap()))

                            }, {
                                handleNetworkError(it)
                                logIn.postValue(false)
                            }
                        )
                )
            }

        }


    }


}