package com.sample.insta.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserInfoResponse(
    @Expose
    @SerializedName("statusCode")
    val statusCode: String,

    @Expose
    @SerializedName("status")
    val status: Int,

    @Expose
    @SerializedName("message")
    val message: String?,

    @Expose
    @SerializedName("data")
    val data: UserDetails
)
