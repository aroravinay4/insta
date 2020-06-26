package com.sample.insta.data.repository

import com.sample.insta.data.local.prefs.Preferences
import com.sample.insta.data.model.User
import com.sample.insta.data.remote.NetWorkService
import com.sample.insta.data.remote.Networking
import com.sample.insta.data.remote.request.LoginRequest
import com.sample.insta.data.remote.request.SignUpRequest
import com.sample.insta.data.remote.request.UpdateUserRequest
import com.sample.insta.data.remote.response.GeneralResponse
import com.sample.insta.data.remote.response.UserDetails
import com.sample.insta.data.remote.response.UserInfoResponse
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val netWorkService: NetWorkService,
    private val preferences: Preferences
) {


    fun saveUserData(user: User) {
        preferences.run {
            setUserId(user.userId)
            setUserEmail(user.userEmail)
            setAccessToken(user.accessToken)
            setUserName(user.userName)
        }
    }


    fun updateUserData(user: UserDetails) {
        preferences.run {
            setUserId(user.id)
            setUserName(user.name)
            setProfilePic(user.profilePicUrl)
        }

    }

        fun removeUser() {
            preferences.clearAllPreference()
        }

        fun getCurrentUserDataFromPref(): User? {
            val userId = preferences.getUserId()
            val userName = preferences.getUserName()
            val accessToken = preferences.getAccessToken()
            val useEmail = preferences.getUserEmail()


            return if (userId !== null && userName !== null && accessToken !== null && useEmail !== null)
                User(accessToken, userId, userName, useEmail, "")
            else
                null
        }

    fun doLogin(email: String, password: String): Single<User> = netWorkService.doLoginCall(
        LoginRequest(email, password)
    ).map {
        User(
            it.accessToken,
            it.userId,
            it.userName,
            it.userEmail,
            it.profilePicUrl
        )
    }

    fun doSignUp(email: String, password: String, name: String): Single<User> =
        netWorkService.doSignUp(
            SignUpRequest(name, email, password)
        ).map {
            User(
                it.accessToken,
                it.userId,
                it.userName,
                it.userEmail,
                it.profilePicUrl
            )
        }


    fun getUsersInfo(user: User): Single<UserDetails> =
        netWorkService.getUserDetails(user.userId, user.accessToken).map {
            it.data
        }

    fun logout(user: User): Single<GeneralResponse> =
        netWorkService.logout(user.userId, user.accessToken).map { it }

    fun updateUser(
        name: String,
        tagLine: String,
        photoUrl: String,
        user: User
    ): Single<GeneralResponse> =
        netWorkService.updateUserDetails(
            UpdateUserRequest(
                name,
                tagLine,
                photoUrl
            ), user.userId,
            user.accessToken
        ).map { it }
}