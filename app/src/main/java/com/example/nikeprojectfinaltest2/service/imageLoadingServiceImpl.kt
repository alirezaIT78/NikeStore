package com.example.nikeprojectfinaltest2.service

import android.annotation.SuppressLint
import com.example.nikeprojectfinaltest2.view.NikeImageView
import com.facebook.drawee.view.SimpleDraweeView

class imageLoadingServiceImpl:ImageLoadingService {
    @SuppressLint("SuspiciousIndentation")
    override fun load(imageview: NikeImageView, url:String) {
        if (imageview is SimpleDraweeView)
        {
            imageview.setImageURI(url)
        }
        else
        throw IllegalStateException("ImageView Must Be SimpleDraweeView")
    }
}