package com.example.nikeprojectfinaltest2.repo

import com.example.nikeprojectfinaltest.data.AddToCartResource
import com.example.nikeprojectfinaltest.data.CartItemCount
import com.example.nikeprojectfinaltest.data.CartListResponse
import com.example.nikeprojectfinaltest.data.MessageResponse
import io.reactivex.Single

interface CartRepository {
   fun getCart():Single<CartListResponse>
   fun addToCart(productId:Int):Single<AddToCartResource>
   fun removeCart(cartItemId:Int):Single<MessageResponse>
   fun cartItemChangeCount(cartItemId: Int,count:Int):Single<AddToCartResource>
   fun getCartCount():Single<CartItemCount>
}