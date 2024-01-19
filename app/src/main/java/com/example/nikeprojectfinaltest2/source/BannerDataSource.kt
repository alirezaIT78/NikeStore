package com.example.nikeprojectfinaltest2.source

import com.example.nikeprojectfinaltest2.data.Banner
import io.reactivex.Single

interface BannerDataSource {
    fun getBanner():Single<List<Banner>>
}