package com.example.nikeprojectfinaltest2.feucher.checkout

import androidx.lifecycle.MutableLiveData
import com.example.nikeprojectfinaltest.data.Checkout
import com.example.nikeprojectfinaltest2.common.baseViewModel
import com.example.nikeprojectfinaltest2.common.myObserver
import com.example.nikeprojectfinaltest2.repo.OrderRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ViewModelCheckout(orderId:Int,orderRepository: OrderRepository):baseViewModel() {
    val checkoutLiveData=MutableLiveData<Checkout>()
   init {
       orderRepository.checkOut(orderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
           .subscribe(object :myObserver<Checkout>(compositeDisposable){
               override fun onSuccess(t: Checkout) {
                   checkoutLiveData.value=t
               }

           })
   }
}