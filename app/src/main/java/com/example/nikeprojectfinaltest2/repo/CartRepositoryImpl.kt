package com.example.nikeprojectfinaltest2.repo

import com.example.nikeprojectfinaltest.data.AddToCartResource
import com.example.nikeprojectfinaltest.data.CartItemCount
import com.example.nikeprojectfinaltest.data.CartListResponse
import com.example.nikeprojectfinaltest.data.MessageResponse
import com.example.nikeprojectfinaltest2.source.CartDataSource
import io.reactivex.Single

class CartRepositoryImpl(val remoteDataSource: CartDataSource):CartRepository {
    override fun getCart(): Single<CartListResponse>
    {
        return remoteDataSource.getCart()
    }

    override fun addToCart(productId: Int): Single<AddToCartResource> {
      return  remoteDataSource.addToCart(productId)
    }

    override fun removeCart(cartItemId: Int): Single<MessageResponse> =remoteDataSource.removeCart(cartItemId)

    override fun cartItemChangeCount(cartItemId: Int, count: Int): Single<AddToCartResource> =remoteDataSource.cartItemChangeCount(cartItemId, count)

    override fun getCartCount(): Single<CartItemCount> =remoteDataSource.getCartCount()
}