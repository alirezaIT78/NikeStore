package com.example.nikeprojectfinaltest2.repo

import androidx.lifecycle.MutableLiveData
import com.example.nikeprojectfinaltest2.data.productData
import com.example.nikeprojectfinaltest2.feucher.product.deleteOrAddItemFavorite
import com.example.nikeprojectfinaltest2.source.ProductDataSource
import com.example.nikeprojectfinaltest2.source.productLocalDataSource
import com.example.nikeprojectfinaltest2.source.productRemoteDataSource
import io.reactivex.Completable
import io.reactivex.Single
var favoriteIdsLiveData=MutableLiveData<List<Int>>()
var favoriteListIds=ArrayList<Int>()
class ProductRepositoryImpl(val productRemoteDataSource: ProductDataSource,val productLocalDataSource: ProductDataSource):ProductRepository {
    override fun getProductFromApi(sort:Int): Single<List<productData>> {
       return productLocalDataSource.getFavoriteProductList().flatMap {favoriteList->
           productRemoteDataSource.getProductFromApi(sort).doOnSuccess {productList->
               var favoriteIds=favoriteList.map {
                   it.id
               }
               if(favoriteListIds.isEmpty())
               {
                   favoriteListIds.addAll(favoriteIds)
                   favoriteIdsLiveData.postValue(favoriteListIds)
               }else{
                   favoriteIdsLiveData.postValue(favoriteListIds)
               }
               productList.forEach {
                   if (favoriteIds.contains(it.id))
                       it.isFavorite=true
               }
           }
       }
    }

    override fun getFavoriteProductList(): Single<List<productData>> {
       return productLocalDataSource.getFavoriteProductList()
    }

    override fun addProductToFavorite(product: productData): Completable {
        deleteOrAddItemFavorite=true
        if (!favoriteListIds.contains(product.id))
        {
            favoriteListIds.add(product.id)
            favoriteIdsLiveData.postValue(favoriteListIds)
        }
       return productLocalDataSource.addProductToFavorite(product)
    }

    override fun deleteFavoriteProduct(product: productData): Completable {
        deleteOrAddItemFavorite=true
        if (favoriteListIds.contains(product.id))
        {
            favoriteListIds.remove(product.id)
            favoriteIdsLiveData.postValue(favoriteListIds)

        }
        return productLocalDataSource.deleteFavoriteProduct(product)
    }


}