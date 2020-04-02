package com.myrealtrip.newsreader.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.myrealtrip.newsreader.utils.GlideApp

@BindingAdapter("bindUrl")
fun bindImageFormUrl(view: ImageView, url: String?) {
    val context = view.context
    if (!url.isNullOrEmpty()) {
        GlideApp.with(context)
            .load(url)
            .thumbnail(0.1f)
            .into(view)
    }
}

@BindingAdapter("bindKeyword")
fun bindTextFormKeyword(view: TextView, keyword: String?) {
    if (keyword.isNullOrEmpty()) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
        view.text = keyword
    }
}