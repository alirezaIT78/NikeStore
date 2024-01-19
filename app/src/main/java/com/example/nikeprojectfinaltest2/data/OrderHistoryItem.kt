package com.example.nikeprojectfinaltest.data

data class OrderHistoryItem(
    val id:Int,
    val payable:Int,
    val order_items:List<OrderItem>
)
