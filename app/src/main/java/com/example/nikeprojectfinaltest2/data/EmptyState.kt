package com.example.nikeprojectfinaltest2.data

import androidx.annotation.StringRes

data class EmptyState(
    val mustShow:Boolean,
    @StringRes val resIdString:Int=0,
    val mustShowBtnLogin:Boolean=false
)
