package com.example.nikeprojectfinaltest2.source

import com.example.nikeprojectfinaltest.data.MessageResponse
import com.example.nikeprojectfinaltest2.data.TokenResponse
import io.reactivex.Completable
import io.reactivex.Single

interface UserDataSource {
    fun login(username:String,password:String): Single<TokenResponse>
    fun singUp(email:String,password: String):Single<MessageResponse>
    fun loadToken()
    fun saveToken(token:String,refreshToken:String)
    fun getUser():String
    fun saveUser(username: String)
    fun signOut()
}