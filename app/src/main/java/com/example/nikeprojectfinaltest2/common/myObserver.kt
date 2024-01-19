package com.example.nikeprojectfinaltest2.common

import android.util.Log
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
var errorConnect=""
abstract class myObserver<T>(val compositeDisposable: CompositeDisposable):SingleObserver<T> {
    override fun onSubscribe(d: Disposable) {
        compositeDisposable.add(d)
    }

    override fun onError(e: Throwable) {
        if (e.message.toString() == "Failed to connect to expertdevelopers.ir/185.94.99.234:80")
        {
            errorConnect="اتصال شما به اینترنت برقرار نیست!"
        }else if (e.message.toString()=="Unable to resolve host \"expertdevelopers.ir\": No address associated with hostname")
        {
            errorConnect="اتصال شما به اینترنت برقرار نیست!"
        }
        else{
            EventBus.getDefault().post(NikeExceptionMapper.mapper(e))
        }
    }
}