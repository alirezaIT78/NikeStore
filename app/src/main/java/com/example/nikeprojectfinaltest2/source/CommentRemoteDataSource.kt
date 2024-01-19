package com.example.nikeprojectfinaltest2.source

import com.example.nikeprojectfinaltest2.data.Comment
import com.example.nikeprojectfinaltest2.service.ApiService
import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.Single

class CommentRemoteDataSource(val apiService: ApiService):CommentDataSource {
    override fun getComment(productId: Int): Single<List<Comment>> {
        return apiService.getComment(productId)
    }

    override fun addComment(title: String, content: String, productId: Int): Completable {
     return  apiService.addComment(JsonObject().apply {
           addProperty("title",title)
           addProperty("content",content)
           addProperty("product_id",productId)
       })
    }
}