package com.myrealtrip.newsreader.utils

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.myrealtrip.newsreader.R

@GlideModule
class MyGlideApp : AppGlideModule() {

    companion object {
        private fun requestOptions() : RequestOptions {
            return RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .centerCrop()
                .signature(ObjectKey(System.currentTimeMillis()))
                .error(R.drawable.no_image)
        }
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDefaultRequestOptions(requestOptions())
    }

}