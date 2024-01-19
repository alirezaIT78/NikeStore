package com.example.nikeprojectfinaltest2.repo

import android.icu.text.CaseMap.Title
import com.example.nikeprojectfinaltest2.data.Comment
import io.reactivex.Completable
import io.reactivex.Single

interface CommentRepository {
    fun getComment(productId:Int):Single<List<Comment>>
    fun addComment(title: String,content:String,productId: Int):Completable
}