package com.example.nikeprojectfinaltest.data

import com.example.nikeprojectfinaltest2.data.productData

data class CartItem(
    val cart_item_id: Int,
    var count: Int,
    val product: productData,
    var progressBarCartItem:Boolean=false
)
