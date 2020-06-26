package com.sample.insta.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.sample.insta.R
import com.sample.insta.di.components.FragmentComponent
import com.sample.insta.ui.base.BaseFragment
import com.sample.insta.ui.home.posts.PostAdapter
import com.sample.insta.ui.main.HomeViewModel
import com.sample.insta.ui.main.MainSharedViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_photo.*
import javax.inject.Inject


class HomeFragment : BaseFragment<HomeFragmentViewModel>() {
    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var postAdapter: PostAdapter

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel

    companion object {
        val TAG: String = "HomeFragment"


        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.fragment_home


    override fun setupView(view: View) {
        rvPost.apply {
            layoutManager = linearLayoutManager
            adapter = postAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    layoutManager?.run {
                        if (this is LinearLayoutManager && itemCount > 0 && itemCount == findLastVisibleItemPosition() + 1) {
                            viewModel.onLoadMore()

                        }
                    }
                }
            })
        }

    }

    override fun injectDependency(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun setupObservers() {
        super.setupObservers()

        viewModel.loading.observe(this, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.posts.observe(this, Observer {
            it.data?.run { postAdapter.appendData(this) }

        })

        mainSharedViewModel.newPost.observe(this, Observer {
            it.getIfNotHandled()?.run { viewModel.onNewPost(this) }
        })

        viewModel.reFreshPost.observe(this, Observer {
            it.data?.run {
                postAdapter.updateList(this)
                rvPost.scrollToPosition(0)
            }

        })

    }
}
