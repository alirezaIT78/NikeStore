package com.example.nikeprojectfinaltest2.service

import android.util.Log
import com.example.nikeprojectfinaltest2.data.TokenContainer
import com.example.nikeprojectfinaltest2.data.TokenResponse
import com.example.nikeprojectfinaltest2.source.CLIENT_ID
import com.example.nikeprojectfinaltest2.source.CLIENT_SECRET
import com.example.nikeprojectfinaltest2.source.UserDataSource
import com.example.nikeprojectfinaltest2.source.UserLocalDataSource
import com.google.gson.JsonObject
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.lang.Exception

class NikeAuthenticator:Authenticator,KoinComponent {
    val apiService:ApiService by inject()
    val userLocalDataSource:UserDataSource by inject()
    override fun authenticate(route: Route?, response: Response): Request? {
        if (TokenContainer.accessToken!=null&&TokenContainer.refreshToken!=null&&!response.request.url.pathSegments.last().equals("token",false))
            try {
                val token=refreshToken()
                if (token.isEmpty())
                    return null

             return response.request.newBuilder().header("Authorization","Bearer $token").build()

            }catch (exption:Exception)
            {
                Log.e("ExceptionRefreshToken", "authenticate: ",exption )
            }
       return null
    }
    fun refreshToken():String{
        val refreshResponse:retrofit2.Response<TokenResponse> =apiService.refreshToken(JsonObject().apply {
            addProperty("grant_type","refresh_token")
            addProperty("refresh_token",TokenContainer.refreshToken)
            addProperty("client_id", CLIENT_ID)
            addProperty("client_secret", CLIENT_SECRET)
        }).execute()
        refreshResponse.body()?.let {
            TokenContainer.update(it.access_token,it.refresh_token)
            userLocalDataSource.saveToken(it.access_token,it.refresh_token)
            return it.refresh_token
        }
        return ""
    }
}