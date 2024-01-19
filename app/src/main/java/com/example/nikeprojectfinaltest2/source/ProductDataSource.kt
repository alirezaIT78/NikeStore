package com.example.nikeprojectfinaltest2.source

import com.example.nikeprojectfinaltest2.data.productData
import io.reactivex.Completable
import io.reactivex.Single

interface ProductDataSource {
    fun getProductFromApi(sortInt: Int):Single<List<productData>>
    fun getFavoriteProductList():Single<List<productData>>
    fun addProductToFavorite(product:productData):Completable
    fun deleteFavoriteProduct(product:productData):Completable
}