package com.sample.insta.data.repository

import com.sample.insta.data.model.User
import com.sample.insta.data.remote.NetWorkService
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class PhotoRepository @Inject constructor(private val netWorkService: NetWorkService) {

    fun uploadPhoto(file: File, user: User): Single<String> {
        return MultipartBody.Part.createFormData(
            "image", file.name,
            RequestBody.create(MediaType.parse("image/*"), file)
        ).run {
            return netWorkService.imageUpload(this, user.userId, user.accessToken)
                .map { it.data.imageUrl }
        }

    }


}