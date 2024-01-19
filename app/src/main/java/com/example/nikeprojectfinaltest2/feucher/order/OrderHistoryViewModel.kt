package com.example.nikeprojectfinaltest2.feucher.order

import androidx.lifecycle.MutableLiveData
import com.example.nikeprojectfinaltest.data.OrderHistoryItem
import com.example.nikeprojectfinaltest2.common.baseViewModel
import com.example.nikeprojectfinaltest2.common.myObserver
import com.example.nikeprojectfinaltest2.repo.OrderRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OrderHistoryViewModel(val orderRepository: OrderRepository):baseViewModel() {
    val orderHistoryItemLiveData=MutableLiveData<List<OrderHistoryItem>>()
    fun getOrders(){
        progressLiveData.value=true
        orderRepository.getOrders().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .doFinally { progressLiveData.value=false }
            .subscribe(object :myObserver<List<OrderHistoryItem>>(compositeDisposable){
                override fun onSuccess(t: List<OrderHistoryItem>) {
                    orderHistoryItemLiveData.value=t
                }
            })
    }
}