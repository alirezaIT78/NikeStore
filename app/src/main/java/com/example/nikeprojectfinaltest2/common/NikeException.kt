package com.example.nikeprojectfinaltest2.common

import android.os.Message
import androidx.annotation.StringRes

class NikeException(val type:Type,@StringRes val userFriendlyMessage:Int=0,val serverMessage: String?=null):Throwable() {
    enum class Type{
        Simple,Auth
    }
}