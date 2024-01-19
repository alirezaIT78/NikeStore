package com.example.nikeprojectfinaltest2.repo

import com.example.nikeprojectfinaltest2.data.productData
import io.reactivex.Completable
import io.reactivex.Single

interface ProductRepository {
    fun getProductFromApi(sort:Int): Single<List<productData>>
    fun getFavoriteProductList(): Single<List<productData>>
    fun addProductToFavorite(product:productData): Completable
    fun deleteFavoriteProduct(product:productData): Completable
}