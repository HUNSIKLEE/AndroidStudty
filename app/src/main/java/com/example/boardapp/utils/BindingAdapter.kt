package com.example.boardapp.utils

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.boardapp.R

object BindingAdapter {

    @BindingAdapter("setImage")
    @JvmStatic
    fun viewClick(view: ImageView, uri: Uri) {
        Glide.with(view.context)
            .load(uri)
            .placeholder(R.drawable.baseline_account_circle_24)
            .error(R.drawable.baseline_account_circle_24)
            .into(view)
    }
}