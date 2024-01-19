package com.example.nikeprojectfinaltest2.repo

import com.example.nikeprojectfinaltest2.data.Banner
import io.reactivex.Single

interface BannerRepository {
    fun getBanner():Single<List<Banner>>
}