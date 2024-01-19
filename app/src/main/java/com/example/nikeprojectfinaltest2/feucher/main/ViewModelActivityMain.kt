package com.example.nikeprojectfinaltest2.feucher.main

import android.annotation.SuppressLint
import com.example.nikeprojectfinaltest.data.CartItemCount
import com.example.nikeprojectfinaltest2.common.baseViewModel
import com.example.nikeprojectfinaltest2.common.myObserver
import com.example.nikeprojectfinaltest2.data.TokenContainer
import com.example.nikeprojectfinaltest2.repo.CartRepository
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

class ViewModelActivityMain(val cartRepository: CartRepository):baseViewModel() {
    val cartItemCount=CartItemCount(0)
    @SuppressLint("SuspiciousIndentation")
    fun getCartCount()
    {
        if (!TokenContainer.accessToken.isNullOrEmpty())
        cartRepository.getCartCount().subscribeOn(Schedulers.io())
            .subscribe(object :myObserver<CartItemCount>(compositeDisposable){
                override fun onSuccess(t: CartItemCount) {
                    EventBus.getDefault().postSticky(t)
                }
            })else{
                EventBus.getDefault().postSticky(cartItemCount)
            }
    }
}