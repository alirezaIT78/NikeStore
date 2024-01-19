package com.example.nikeprojectfinaltest2.repo

import com.example.nikeprojectfinaltest.data.Checkout
import com.example.nikeprojectfinaltest.data.OrderHistoryItem
import com.example.nikeprojectfinaltest.data.SubmitOrderRequest
import com.example.nikeprojectfinaltest2.source.OrderDataSource
import io.reactivex.Single

class OrderRepositoryImpl(val remoteDataSource: OrderDataSource):OrderRepository {
    override fun submitOrder(
        firstname: String,
        lastname: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderRequest> =
        remoteDataSource.submitOrder(firstname, lastname, postalCode, phoneNumber, address, paymentMethod)


    override fun checkOut(orderId: Int): Single<Checkout> = remoteDataSource.checkOut(orderId)
    override fun getOrders(): Single<List<OrderHistoryItem>> {
        return remoteDataSource.getOrders()
    }
}