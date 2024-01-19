package com.example.nikeprojectfinaltest2.feucher.auth

import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.nikeprojectfinaltest2.common.baseViewModel
import com.example.nikeprojectfinaltest2.repo.UserRepository
import io.reactivex.Completable

class viewModelUser(private val userRepository:UserRepository):baseViewModel() {
    fun login(username:String,password:String):Completable{
       return userRepository.login(username,password)
    }
    fun singUp(email:String,password: String):Completable
    {
       return userRepository.singUp(email, password)
    }
}