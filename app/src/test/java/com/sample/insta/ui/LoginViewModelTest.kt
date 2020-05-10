package com.sample.insta.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.sample.insta.R
import com.sample.insta.data.model.User
import com.sample.insta.data.repository.UserRepository
import com.sample.insta.ui.login.LoginViewModel
import com.sample.insta.utils.common.Event
import com.sample.insta.utils.common.Resource
import com.sample.insta.utils.network.NetworkHelper
import com.sample.insta.utils.rx.TestSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var logInObserver: Observer<Boolean>

    @Mock
    private lateinit var launchHomeObserver: Observer<Event<Map<String, String>>>

    @Mock
    private lateinit var messageStringIdObserver: Observer<Resource<Int>>

    private lateinit var testScheduler: TestScheduler

    private lateinit var loginViewModel: LoginViewModel


    @Before
    fun setUp() {
        val compositeDisposable = CompositeDisposable()
        testScheduler = TestScheduler()
        val testSchedulerProvider = TestSchedulerProvider(testScheduler)
        loginViewModel = LoginViewModel(
            testSchedulerProvider,
            compositeDisposable,
            networkHelper,
            userRepository
        )

        loginViewModel.logIn.observeForever(logInObserver)
        loginViewModel.launchHomeActivity.observeForever(launchHomeObserver)
        loginViewModel.messageStringId.observeForever(messageStringIdObserver)
    }

    @Test
    fun giveServerResponse200_whenLogin_shouldLaunchHomeActivity() {
        val email = "test@gmail.com"
        val password = "password"
        val user = User("id", "test", email, password, "accessToken")
        loginViewModel.emailField.value = email
        loginViewModel.passwordField.value = password

        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()
        doReturn(Single.just(user))
            .`when`(userRepository)
            .doLogin(email, password)

        loginViewModel.login()
        testScheduler.triggerActions()
        verify(userRepository).saveUserData(user)
        assert(loginViewModel.logIn.value == false)
        assert(loginViewModel.launchHomeActivity.value == Event(hashMapOf<String, String>()))
        verify(logInObserver).onChanged(true)
        verify(logInObserver).onChanged(false)
        verify(launchHomeObserver).onChanged(Event(hashMapOf()))

    }

    @Test
    fun giveNoInternet_whenLogin_shouldShowNetworkError() {
        val email = "test@gmail.com"
        val password = "password"
        loginViewModel.emailField.value = email
        loginViewModel.passwordField.value = password
        doReturn(false).`when`(networkHelper).isNetworkConnected()
        loginViewModel.login()
        assert(loginViewModel.messageStringId.value == Resource.error(R.string.network_connection_error))
        verify(messageStringIdObserver).onChanged(Resource.error(R.string.network_connection_error))

    }


    @After
    fun tearDown() {
        loginViewModel.logIn.removeObserver(logInObserver)
        loginViewModel.launchHomeActivity.removeObserver(launchHomeObserver)
        loginViewModel.messageStringId.removeObserver(messageStringIdObserver)
    }

}