package com.sample.insta.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("tagline")
    val tagLine: String,

    @Expose
    @SerializedName("profilePicUrl")
    var profilePicUrl: String?
)