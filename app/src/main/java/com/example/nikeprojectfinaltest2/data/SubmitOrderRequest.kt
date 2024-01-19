package com.example.nikeprojectfinaltest.data

data class SubmitOrderRequest(
    val bank_gateway_url: String,
    val order_id: Int
)