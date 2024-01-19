package com.example.nikeprojectfinaltest2.service

import com.example.nikeprojectfinaltest2.view.NikeImageView

interface ImageLoadingService {
    fun load(imageview:NikeImageView,url:String)
}