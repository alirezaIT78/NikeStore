package com.example.nikeprojectfinaltest2.common

import com.example.nikeprojectfinaltest2.R
import org.json.JSONObject


class NikeExceptionMapper {
    companion object{
        fun mapper(throwable: Throwable):NikeException
        {
            if (throwable is retrofit2.HttpException)
            {
                val errorJsonObject=JSONObject(throwable.response()?.errorBody()!!.string())
                val errorMessageJson=errorJsonObject.getString("message")
                return NikeException(if (throwable.code()==401)NikeException.Type.Auth else NikeException.Type.Simple, serverMessage = errorMessageJson)
            }
            return NikeException(NikeException.Type.Simple, R.string.unKnownError)
        }
    }

}