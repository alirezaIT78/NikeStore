package com.example.nikeprojectfinaltest2.feucher.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.nikeprojectfinaltest2.common.NikeCompletable
import com.example.nikeprojectfinaltest2.common.baseViewModel
import com.example.nikeprojectfinaltest2.common.myObserver
import com.example.nikeprojectfinaltest2.data.Banner
import com.example.nikeprojectfinaltest2.data.productData
import com.example.nikeprojectfinaltest2.data.sortByLatestProduct
import com.example.nikeprojectfinaltest2.data.sortByPopularProduct
import com.example.nikeprojectfinaltest2.repo.BannerRepository
import com.example.nikeprojectfinaltest2.repo.ProductRepository
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class mainViewModel(val productRepository: ProductRepository,val bannerRepository: BannerRepository):baseViewModel() {
    val bannerLiveData=MutableLiveData<List<Banner>>()
    val productPopLiveData=MutableLiveData<List<productData>>()

   val productLiveData=MutableLiveData<List<productData>>()
    init {
        LoadAllItemProduct()
    }
    fun LoadAllItemProduct()
    {
        progressLiveData.value=true
        productRepository.getProductFromApi(sortByLatestProduct).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                progressLiveData.value=false
            }.subscribe(object : myObserver<List<productData>>(compositeDisposable) {
                override fun onSuccess(t: List<productData>) {
                    productLiveData.value=t
                }

            })

        productRepository.getProductFromApi(sortByPopularProduct).subscribeOn(Schedulers.io()).
        observeOn(AndroidSchedulers.mainThread()).subscribe(object :myObserver<List<productData>>(compositeDisposable){
            override fun onSuccess(t: List<productData>) {
                productPopLiveData.value=t
            }

        })
    }
    fun getBanner()
    {
        bannerRepository.getBanner().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :myObserver<List<Banner>>(compositeDisposable){
                override fun onSuccess(t: List<Banner>) {
                    bannerLiveData.value=t
                }

            })
    }
    fun addOrRemoveFavorite(productData: productData)
    {
        if (productData.isFavorite)
        productRepository.deleteFavoriteProduct(productData).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
            .subscribe(object :NikeCompletable(compositeDisposable){
                override fun onComplete() {
                    productData.isFavorite=false
                }
            })else{
                productRepository.addProductToFavorite(productData).subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io()).subscribe(object :NikeCompletable(compositeDisposable){
                        override fun onComplete() {
                            productData.isFavorite=true
                        }
                    })
            }
    }
}