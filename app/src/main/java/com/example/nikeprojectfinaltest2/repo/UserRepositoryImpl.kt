package com.example.nikeprojectfinaltest2.repo

import com.example.nikeprojectfinaltest2.data.TokenContainer
import com.example.nikeprojectfinaltest2.data.TokenResponse
import com.example.nikeprojectfinaltest2.source.UserDataSource
import io.reactivex.Completable

class UserRepositoryImpl(val localDataSource:UserDataSource,val remoteDataSource:UserDataSource):UserRepository {
    override fun login(username: String, password: String): Completable =
        remoteDataSource.login(username,password).doOnSuccess {
            loginSuccess(username,it)
        }.ignoreElement()


    override fun singUp(email: String, password: String): Completable {
       return remoteDataSource.singUp(email,password).flatMap {
           remoteDataSource.login(email,password)
       }.doOnSuccess {
           loginSuccess(email,it)
       }.ignoreElement()
    }

    override fun loadToken() {
        localDataSource.loadToken()
    }

    override fun getUsername(): String {
       return localDataSource.getUser()
    }

    override fun signOut() {
        localDataSource.signOut()
    }

    fun loginSuccess(username: String,tokenResponse: TokenResponse)
    {
        localDataSource.saveToken(tokenResponse.access_token,tokenResponse.refresh_token)
        TokenContainer.update(tokenResponse.access_token,tokenResponse.refresh_token)
        localDataSource.saveUser(username)
    }
}