package com.sample.insta.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @Expose
    @SerializedName("accessToken")
    val accessToken: String,

    @Expose
    @SerializedName("userId")
    val userId: String,

    @Expose
    @SerializedName("userName")
    val userName: String,

    @Expose
    @SerializedName("userEmail")
    val userEmail: String,

    @Expose
    @SerializedName("profilePicUrl")
    var profilePicUrl: String?
)
