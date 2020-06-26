package com.sample.insta.ui.profile

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.sample.insta.R
import com.sample.insta.data.remote.Networking
import com.sample.insta.data.remote.response.UserDetails
import com.sample.insta.di.components.FragmentComponent
import com.sample.insta.ui.base.BaseFragment
import com.sample.insta.ui.login.LoginActivity
import com.sample.insta.ui.profile.editprofile.EditProfileActivity
import com.sample.insta.utils.common.Event
import com.sample.insta.utils.common.GlideHelper
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_login_screen.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.progressBar
import kotlinx.android.synthetic.main.layout_post_item_list.*
import kotlinx.android.synthetic.main.layout_post_item_list.view.*
import java.util.*


class ProfileFragment : BaseFragment<ProfileViewModel>() {


    companion object {
        val TAG: String = "profileFragment"


        fun newInstance(): ProfileFragment {
            val args = Bundle()
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var headers: Map<String, String>? = null
    private var userDetails: UserDetails? = null
    private var intent: Intent? = null
    override fun provideLayoutId(): Int = R.layout.fragment_profile

    override fun injectDependency(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun setupView(view: View) {


        tVProfile.setOnClickListener {
            intent = Intent(context, EditProfileActivity::class.java)
            intent!!.putExtra("data", userDetails)
            startActivity(intent)
        }
        headers = mapOf(
            Pair(Networking.HEADER_ACCESS_TOKEN, viewModel.user.accessToken),
            Pair(Networking.HEADER_USER_ID, viewModel.user.userId),
            Pair(Networking.HEADER_API_KEY, Networking.API_KEY)
        )
        tVLogout.setOnClickListener { viewModel.logout() }


    }

    override fun onResume() {
        super.onResume()
        viewModel.getDetails()
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.loading.observe(this, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE

        })
        viewModel.name.observe(this, Observer {
            ivUserName.text = it

        })
        viewModel.tagLine.observe(this, Observer {
            tvTagLine.text = it
        })

        viewModel.launchEditProfileActivity.observe(this, Observer {
            it.getIfNotHandled()?.run { userDetails = this }

        })


        viewModel.profilePicUrl.observe(this, Observer {
            it?.run {
                val glideRequest =
                    Glide.with(activity!!)
                        .load(GlideHelper.getProtectedUrl(it, headers!!))
                        .apply(RequestOptions.circleCropTransform())
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_selected))

                glideRequest.into(ivUserPic)
            }

        })


        viewModel.launchLoginActivity.observe(this, Observer {
            intent = Intent(context, LoginActivity::class.java)
            intent!!.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        })
    }
}
