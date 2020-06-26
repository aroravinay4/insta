package com.sample.insta.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserDetails(

    @Expose
    @SerializedName("id")
    val id: String,

    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("tagline")
    val tagLine: String,

    @Expose
    @SerializedName("profilePicUrl")
    var profilePicUrl: String?
):Serializable
