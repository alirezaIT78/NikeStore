package com.example.nikeprojectfinaltest2.source

import com.example.nikeprojectfinaltest2.data.productData
import com.example.nikeprojectfinaltest2.service.ApiService
import com.example.nikeprojectfinaltest2.service.RetrofitApiService
import io.reactivex.Completable
import io.reactivex.Single

class productRemoteDataSource(val apiService: ApiService):ProductDataSource {
    override fun getProductFromApi(sort:Int): Single<List<productData>> {
        return apiService.getListOfProduct(sort.toString())
    }

    override fun getFavoriteProductList(): Single<List<productData>> {
        TODO("Not yet implemented")
    }

    override fun addProductToFavorite(product: productData): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteFavoriteProduct(product: productData): Completable {
        TODO("Not yet implemented")
    }


}