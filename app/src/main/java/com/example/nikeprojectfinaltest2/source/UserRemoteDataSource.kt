package com.example.nikeprojectfinaltest2.source

import com.example.nikeprojectfinaltest.data.MessageResponse
import com.example.nikeprojectfinaltest2.data.TokenResponse
import com.example.nikeprojectfinaltest2.service.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single
const val CLIENT_ID=2
const val CLIENT_SECRET="kyj1c9sVcksqGU4scMX7nLDalkjp2WoqQEf8PKAC"
class UserRemoteDataSource(val apiService: ApiService):UserDataSource {
    override fun login(username: String, password: String): Single<TokenResponse> =
        apiService.login(JsonObject().apply {
            addProperty("username",username)
            addProperty("password",password)
            addProperty("grant_type","password")
            addProperty("client_id", CLIENT_ID)
            addProperty("client_secret", CLIENT_SECRET)
        })


    override fun singUp(email: String, password: String): Single<MessageResponse> =
        apiService.singUp(JsonObject().apply {
            addProperty("email",email)
            addProperty("password",password)
        })


    override fun loadToken() {
        TODO("Not yet implemented")
    }

    override fun saveToken(token: String, refreshToken: String) {
        TODO("Not yet implemented")
    }

    override fun getUser(): String {
        TODO("Not yet implemented")
    }

    override fun saveUser(username: String) {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }
}