package com.fullpagedeveloper.submission3githubusers.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("avatar")
fun avatar(imageView: ImageView, avatar: String) =
    Glide.with(imageView)
        .load(avatar)
        .into(imageView)
