package com.example.nikeprojectfinaltest2.feucher.list

import androidx.lifecycle.MutableLiveData
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.common.baseViewModel
import com.example.nikeprojectfinaltest2.common.myObserver
import com.example.nikeprojectfinaltest2.data.productData
import com.example.nikeprojectfinaltest2.repo.ProductRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class viewModelProductList(var sort:Int,val productRepository: ProductRepository):baseViewModel() {
    val productListLiveData=MutableLiveData<List<productData>>()
    val sortLiveData=MutableLiveData<Int>()
    val sortTitle= arrayOf(R.string.sortByLatestProduct,R.string.sortByPopProduct,R.string.sortByPriceHighToLow,R.string.sortByPriceLowToHigh)
    init {
        getProductList()
        sortLiveData.value=sortTitle[sort]
    }
    fun getProductList()
    {
        progressLiveData.value=true
        productRepository.getProductFromApi(sort).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .doFinally { progressLiveData.value=false }.subscribe(object :myObserver<List<productData>>(compositeDisposable){
                override fun onSuccess(t: List<productData>) {
                    productListLiveData.value=t
                }

            })
    }
    fun onSelectedChangeByUser(sort: Int)
    {
        this.sort=sort
        getProductList()
        sortLiveData.value=sortTitle[sort]
    }
}