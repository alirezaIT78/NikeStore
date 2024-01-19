package com.example.nikeprojectfinaltest2.feucher.comment

import androidx.lifecycle.MutableLiveData
import com.example.nikeprojectfinaltest2.common.baseViewModel
import com.example.nikeprojectfinaltest2.common.myObserver
import com.example.nikeprojectfinaltest2.data.Comment
import com.example.nikeprojectfinaltest2.repo.CommentRepository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class viewModelComment(val productId:Int,val commentRepository: CommentRepository):baseViewModel() {
    val commentAllViewModel=MutableLiveData<List<Comment>>()
    init {
        progressLiveData.value=true
        getComment()
    }
    fun getComment()
    {
        commentRepository.getComment(productId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).doFinally { progressLiveData.value=false }.subscribe(object :myObserver<List<Comment>>(compositeDisposable){
                override fun onSuccess(t: List<Comment>) {
                    commentAllViewModel.value=t
                }

            })
    }
    fun addComment(title:String,content:String):Completable
    {
        return commentRepository.addComment(title, content, productId)
    }
}