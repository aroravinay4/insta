package com.sample.insta.ui.home.posts

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.sample.insta.data.model.Post
import com.sample.insta.ui.base.BaseAdapter

class PostAdapter(parentLifeCycle: Lifecycle, post: ArrayList<Post>) :
    BaseAdapter<Post, PostItemViewHolder>(parentLifeCycle, post) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemViewHolder =
        PostItemViewHolder(parent)


}