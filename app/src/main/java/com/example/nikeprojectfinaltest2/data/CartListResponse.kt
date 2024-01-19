package com.example.nikeprojectfinaltest.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class CartListResponse(
    val cart_items: List<CartItem>,
    val payable_price: Int,
    val shipping_cost: Int,
    val total_price: Int
)
@Parcelize
data class Purchase(var payable_price: Int,var total_price: Int, var shipping_cost: Int ) :
    Parcelable