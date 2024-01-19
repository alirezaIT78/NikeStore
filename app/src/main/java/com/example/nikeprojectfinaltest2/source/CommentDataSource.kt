package com.example.nikeprojectfinaltest2.source

import com.example.nikeprojectfinaltest2.data.Comment
import io.reactivex.Completable
import io.reactivex.Single

interface CommentDataSource {
    fun getComment(productId:Int):Single<List<Comment>>
    fun addComment(title:String,content:String,productId: Int):Completable
}