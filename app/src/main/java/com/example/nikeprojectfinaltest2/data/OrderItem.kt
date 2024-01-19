package com.example.nikeprojectfinaltest.data

import com.example.nikeprojectfinaltest2.data.productData

data class OrderItem(
    val count:Int,
    val discount:Int,
    val id:Int,
    val order_id:Int,
    val price:Int,
    val product: productData,
    val product_id:Int
)
