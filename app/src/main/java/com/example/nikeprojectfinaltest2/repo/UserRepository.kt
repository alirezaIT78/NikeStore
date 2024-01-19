package com.example.nikeprojectfinaltest2.repo

import com.example.nikeprojectfinaltest2.data.TokenResponse
import io.reactivex.Completable

interface UserRepository {
    fun login(username:String,password:String):Completable
    fun singUp(email:String,password: String):Completable
    fun loadToken()
    fun getUsername():String
    fun signOut()
}