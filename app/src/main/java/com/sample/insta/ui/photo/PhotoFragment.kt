package com.sample.insta.ui.photo

import android.app.Activity
import android.content.Intent
import android.hardware.Camera
import android.hardware.camera2.CameraManager

import android.os.Bundle
import android.util.EventLog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.sample.insta.R
import com.sample.insta.di.components.FragmentComponent
import com.sample.insta.ui.base.BaseFragment
import com.sample.insta.ui.home.HomeFragment
import com.sample.insta.ui.main.MainSharedViewModel
import com.sample.insta.utils.common.Event
import kotlinx.android.synthetic.main.fragment_photo.*
import java.io.FileNotFoundException
import java.lang.Exception
import javax.inject.Inject


class PhotoFragment : BaseFragment<PhotoViewModel>() {

    @Inject
    lateinit var camera: com.mindorks.paracamera.Camera

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel

    companion object {
        val TAG: String = "PhotoFragment"
        const val RESULT_GALLERY_IMG = 1001

        fun newInstance(): PhotoFragment {
            val args = Bundle()
            val fragment = PhotoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.fragment_photo

    override fun injectDependency(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun setupView(view: View) {
        view_gallery.setOnClickListener {
            Intent(Intent.ACTION_PICK).apply { type = "image/*" }
                .run { startActivityForResult(this, RESULT_GALLERY_IMG) }
        }

        view_camera.setOnClickListener {
            try {
                camera.takePicture()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.loading.observe(this, Observer {
            pb_loading.visibility = if (it) View.VISIBLE else View.GONE

        })

        viewModel.post.observe(this, Observer {
            it.getIfNotHandled()?.run {
                mainSharedViewModel.newPost.postValue(Event(this))
                mainSharedViewModel.onHomeRedirect()
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RESULT_GALLERY_IMG -> {
                    try {
                        data?.data?.let {
                            activity?.contentResolver?.openInputStream(it)?.run {
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
