package com.sample.insta.data.remote

import com.sample.insta.data.remote.request.LoginRequest
import com.sample.insta.data.remote.request.SignUpRequest
import com.sample.insta.data.remote.response.LoginResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface NetWorkService {

    @POST(EndPoint.LOGIN)
    fun doLoginCall(
        @Body request: LoginRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): Single<LoginResponse>

    @POST(EndPoint.SIGN_UP)
    fun doSignUp(
        @Body request: SignUpRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): Single<LoginResponse>


}