package com.example.nikeprojectfinaltest2.data

object TokenContainer {
    var accessToken:String?=null
        private set
    var refreshToken:String?=null
        private set
fun update(token:String?,RefreshToken:String?)
 {
    this.accessToken=token
     this.refreshToken=RefreshToken
 }
}