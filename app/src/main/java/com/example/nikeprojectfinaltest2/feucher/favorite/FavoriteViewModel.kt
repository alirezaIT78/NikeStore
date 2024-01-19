package com.example.nikeprojectfinaltest2.feucher.favorite

import androidx.lifecycle.MutableLiveData
import com.example.nikeprojectfinaltest2.common.baseViewModel
import com.example.nikeprojectfinaltest2.common.myObserver
import com.example.nikeprojectfinaltest2.data.productData
import com.example.nikeprojectfinaltest2.repo.ProductRepository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FavoriteViewModel(val productRepository: ProductRepository):baseViewModel() {
    val favoriteProductLiveData=MutableLiveData<List<productData>>()
    fun getFavoriteProduct()
    {
        progressLiveData.value=true
        productRepository.getFavoriteProductList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .doFinally { progressLiveData.value=false }
            .subscribe(object :myObserver<List<productData>>(compositeDisposable){
                override fun onSuccess(t: List<productData>) {
                    favoriteProductLiveData.value=t
                }
            })
    }
    fun removeFavorite(product:productData):Completable
    {
        return productRepository.deleteFavoriteProduct(product)
    }
}