package com.example.nikeprojectfinaltest2.source

import com.example.nikeprojectfinaltest2.data.Banner
import com.example.nikeprojectfinaltest2.service.ApiService
import io.reactivex.Single

class BannerRemoteDataSource(val apiService: ApiService):BannerDataSource {
    override fun getBanner(): Single<List<Banner>> {
       return apiService.getBanner()
    }
}