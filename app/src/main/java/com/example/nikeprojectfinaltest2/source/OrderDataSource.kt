package com.example.nikeprojectfinaltest2.source

import com.example.nikeprojectfinaltest.data.Checkout
import com.example.nikeprojectfinaltest.data.OrderHistoryItem
import com.example.nikeprojectfinaltest.data.SubmitOrderRequest
import io.reactivex.Single

interface OrderDataSource {
    fun submitOrder(firstname:String,lastname:String,postalCode:String,phoneNumber:String,address:String,paymentMethod:String):Single<SubmitOrderRequest>
    fun checkOut(orderId:Int):Single<Checkout>
    fun getOrders():Single<List<OrderHistoryItem>>
}