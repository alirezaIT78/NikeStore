package com.example.nikeprojectfinaltest2.repo

import com.example.nikeprojectfinaltest2.data.Banner
import com.example.nikeprojectfinaltest2.source.BannerDataSource
import com.example.nikeprojectfinaltest2.source.BannerRemoteDataSource
import io.reactivex.Single

class BannerRepositoryImpel(val remoteDataSource: BannerDataSource):BannerRepository {
    override fun getBanner(): Single<List<Banner>> {
        return remoteDataSource.getBanner()
    }
}