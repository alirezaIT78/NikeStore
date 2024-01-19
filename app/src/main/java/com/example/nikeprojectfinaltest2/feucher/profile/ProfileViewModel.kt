package com.example.nikeprojectfinaltest2.feucher.profile

import com.example.nikeprojectfinaltest2.common.baseViewModel
import com.example.nikeprojectfinaltest2.data.TokenContainer
import com.example.nikeprojectfinaltest2.feucher.main.ViewModelActivityMain
import com.example.nikeprojectfinaltest2.repo.UserRepository

class ProfileViewModel(val userRepository: UserRepository,val cartCount:ViewModelActivityMain):baseViewModel() {
    val username:String
        get() = userRepository.getUsername()
    val isSignIn :Boolean
        get() = TokenContainer.accessToken!=null

    fun signOut()
    {userRepository.signOut()}

   fun getCartCount()
   {
       cartCount.getCartCount()
   }
}