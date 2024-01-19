package com.example.nikeprojectfinaltest2.source

import com.example.nikeprojectfinaltest.data.AddToCartResource
import com.example.nikeprojectfinaltest.data.CartItemCount
import com.example.nikeprojectfinaltest.data.CartListResponse
import com.example.nikeprojectfinaltest.data.MessageResponse
import com.example.nikeprojectfinaltest2.service.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single

class CartRemoteDataSource(val apiService: ApiService):CartDataSource {
    override fun getCart(): Single<CartListResponse> =apiService.getCart()

    override fun addToCart(productId: Int): Single<AddToCartResource> = apiService.addToCart(JsonObject()
        .apply { addProperty("product_id",productId) })


    override fun removeCart(cartItemId: Int): Single<MessageResponse> =apiService.removeCart(JsonObject().apply {
        addProperty("cart_item_id",cartItemId)
    })

    override fun cartItemChangeCount(cartItemId: Int, count: Int): Single<AddToCartResource> =apiService.changeCartCount(JsonObject().apply {
        addProperty("cart_item_id",cartItemId)
        addProperty("count",count)
    })

    override fun getCartCount(): Single<CartItemCount> =apiService.getCountCart()


}