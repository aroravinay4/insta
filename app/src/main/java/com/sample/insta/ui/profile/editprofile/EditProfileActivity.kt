package com.sample.insta.ui.profile.editprofile

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.internal.ContextUtils.getActivity
import com.sample.insta.R
import com.sample.insta.data.remote.Networking
import com.sample.insta.data.remote.response.UserDetails
import com.sample.insta.di.components.ActivityComponent
import com.sample.insta.ui.base.BaseActivity
import com.sample.insta.ui.photo.PhotoFragment
import com.sample.insta.utils.common.GlideHelper
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception
import javax.inject.Inject


class EditProfileActivity : BaseActivity<EditProfileViewModel>() {

    override fun provideLayoutId(): Int = R.layout.activity_edit_profile

    override fun injectDependency(activity: ActivityComponent) = activity.inject(this)

    @Inject
    lateinit var camera: com.mindorks.paracamera.Camera

    companion object {
        const val RESULT_GALLERY_IMG = 1001
    }


    override fun setUpView(savedInstanceState: Bundle?) {
        if (intent.extras != null) {
            val user = intent.getSerializableExtra("data") as UserDetails?
            viewModel.getDataFromBundle(user)
        }


        iVClose.setOnClickListener { finish() }
        tVHeader.setOnClickListener {

        }

        iVChangePhoto.setOnClickListener {
            startDialog()
        }


    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.loading.observe(this, Observer {
            if (!it) finish()
        })

        viewModel.name.observe(this, Observer {
            et_name.setText(it)
        })

        viewModel.profilePicUrl.observe(this, Observer {
            it?.run {
                val glideRequest =
                    Glide.with(iVChangePhoto)
                        .load(GlideHelper.getProtectedUrl(url, headers))
                        .apply(RequestOptions.circleCropTransform())
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_selected))


                if (placeholderWidth > 0 && placeholderHeight > 0) {
                    val params = iVChangePhoto.layoutParams as ViewGroup.LayoutParams
                    params.width = placeholderWidth
                    params.height = placeholderHeight
                    iVChangePhoto.layoutParams = params
                    glideRequest
                        .apply(RequestOptions.overrideOf(placeholderWidth, placeholderHeight))
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_unselected))
                }
                glideRequest.into(iVChangePhoto)
            }
        })

        viewModel.tagLine.observe(this, Observer {
            et_tagLine.setText(it)
        })

        viewModel.localImagePath.observe(this, Observer {
            Glide.with(iVChangePhoto)
                .load(it)
                .placeholder(R.drawable.ic_profile_selected).circleCrop()
                .into(iVChangePhoto)
        })


    }

    private fun startDialog() {
        val myAlertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        myAlertDialog.setTitle("Upload Pictures Option")
        myAlertDialog.setMessage("How do you want to set your picture?")
        myAlertDialog.setPositiveButton("Gallery",
            DialogInterface.OnClickListener { arg0, arg1 ->
                Intent(Intent.ACTION_PICK).apply { type = "image/*" }
                    .run { startActivityForResult(this, RESULT_GALLERY_IMG) }
            })
        myAlertDialog.setNegativeButton("Camera",
            DialogInterface.OnClickListener { arg0, arg1 ->
                try {
                    camera.takePicture()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
        myAlertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RESULT_GALLERY_IMG -> {
                    try {
                        data?.data?.let {
                            this?.contentResolver?.openInputStream(it)?.run {
                                viewModel.onGalleryImageSelected(this)
                            }
                        } ?: showMessage(R.string.try_again)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                        showMessage(R.string.try_again)
                    }
                }

                com.mindorks.paracamera.Camera.REQUEST_TAKE_PHOTO -> {

                    viewModel.onCameraImageTaken { camera.cameraBitmapPath }
                }
            }


        }
    }

}
