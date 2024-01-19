package com.example.nikeprojectfinaltest2.source

import android.content.SharedPreferences
import com.example.nikeprojectfinaltest.data.MessageResponse
import com.example.nikeprojectfinaltest2.data.TokenContainer
import com.example.nikeprojectfinaltest2.data.TokenResponse
import io.reactivex.Single
const val AccessToken="access_token"
const val RefreshToken="refresh_token"
class UserLocalDataSource(val sharedPreferences: SharedPreferences):UserDataSource {
    override fun login(username: String, password: String): Single<TokenResponse> {
        TODO("Not yet implemented")
    }

    override fun singUp(email: String, password: String): Single<MessageResponse> {
        TODO("Not yet implemented")
    }

    override fun loadToken() {
        TokenContainer.update(sharedPreferences.getString(AccessToken,null),
            sharedPreferences.getString(RefreshToken,null))
    }

    override fun saveToken(token: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString(AccessToken, token)
            putString(RefreshToken,refreshToken)
        }.apply()
    }

    override fun getUser(): String {
       return sharedPreferences.getString("username","")?:""
    }

    override fun saveUser(username: String) {
        sharedPreferences.edit().apply {
            putString("username",username)
        }.apply()
    }

    override fun signOut() {
        sharedPreferences.edit().clear().apply()
        TokenContainer.update(null,null)
    }
}