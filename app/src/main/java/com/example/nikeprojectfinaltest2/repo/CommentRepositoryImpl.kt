package com.example.nikeprojectfinaltest2.repo

import com.example.nikeprojectfinaltest2.data.Comment
import com.example.nikeprojectfinaltest2.source.CommentDataSource
import io.reactivex.Completable
import io.reactivex.Single

class CommentRepositoryImpl(val commentRemoteSource: CommentDataSource):CommentRepository {
    override fun getComment(productId: Int): Single<List<Comment>> {
       return commentRemoteSource.getComment(productId)
    }

    override fun addComment(title: String, content: String, productId: Int): Completable {
       return commentRemoteSource.addComment(title, content, productId)
    }
}