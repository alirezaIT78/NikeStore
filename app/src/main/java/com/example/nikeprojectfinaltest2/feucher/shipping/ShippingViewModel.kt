package com.example.nikeprojectfinaltest2.feucher.shipping

import com.example.nikeprojectfinaltest.data.Checkout
import com.example.nikeprojectfinaltest.data.SubmitOrderRequest
import com.example.nikeprojectfinaltest2.common.baseViewModel
import com.example.nikeprojectfinaltest2.repo.OrderRepository
import io.reactivex.Single
const val PAYMENT_ONLINE="online"
const val PAYMENT_DELIVERY="cash_on_delivery"
class ShippingViewModel(val orderRepository: OrderRepository):baseViewModel() {
    fun submitOrder(firstname:String,lastname:String,postalCode:String,phoneNumber:String,address:String,paymentMethod:String):Single<SubmitOrderRequest>
    {
       return orderRepository.submitOrder(firstname, lastname, postalCode, phoneNumber, address, paymentMethod)
    }
}