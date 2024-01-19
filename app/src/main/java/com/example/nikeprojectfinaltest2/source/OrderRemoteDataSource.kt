package com.example.nikeprojectfinaltest2.source

import com.example.nikeprojectfinaltest.data.Checkout
import com.example.nikeprojectfinaltest.data.OrderHistoryItem
import com.example.nikeprojectfinaltest.data.SubmitOrderRequest
import com.example.nikeprojectfinaltest2.service.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single

class OrderRemoteDataSource(val apiService: ApiService):OrderDataSource {
    override fun submitOrder(
        firstname: String,
        lastname: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderRequest> {
        return apiService.submitOrder(JsonObject().apply {
            addProperty("first_name",firstname)
            addProperty("last_name",lastname)
            addProperty("postal_code",postalCode)
            addProperty("mobile",phoneNumber)
            addProperty("address",address)
            addProperty("payment_method",paymentMethod)
        })
    }

    override fun checkOut(orderId: Int): Single<Checkout> {
       return apiService.orderCheckOut(orderId)
    }

    override fun getOrders(): Single<List<OrderHistoryItem>> {
        return apiService.getOrders()
    }
}