package com.example.nikeprojectfinaltest2.feucher.cart

import androidx.lifecycle.MutableLiveData
import com.example.nikeprojectfinaltest.data.CartItem
import com.example.nikeprojectfinaltest.data.CartItemCount
import com.example.nikeprojectfinaltest.data.CartListResponse
import com.example.nikeprojectfinaltest.data.Purchase
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.common.baseViewModel
import com.example.nikeprojectfinaltest2.common.myObserver
import com.example.nikeprojectfinaltest2.data.EmptyState
import com.example.nikeprojectfinaltest2.data.TokenContainer
import com.example.nikeprojectfinaltest2.repo.CartRepository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

class ViewModelCart(val repository:CartRepository):baseViewModel() {
    val cartItemsLiveData=MutableLiveData<List<CartItem>>()
    val purchaseDetailsLiveData=MutableLiveData<Purchase>()
    val cartEmptyStateLiveData=MutableLiveData<EmptyState>()
   private fun getCart(){
       if (!TokenContainer.accessToken.isNullOrEmpty())
       {
          cartEmptyStateLiveData.value= EmptyState(false)
           progressLiveData.value=true
           repository.getCart().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doFinally {
               progressLiveData.value=false
           }.subscribe(object :myObserver<CartListResponse>(compositeDisposable)
           {
               override fun onSuccess(t: CartListResponse) {
                   if (t.cart_items.isNotEmpty())
                   {
                       cartItemsLiveData.value=t.cart_items
                       purchaseDetailsLiveData.value=Purchase(t.payable_price,t.total_price,t.shipping_cost)
                       calculatePurchase()
                   }else{
                       cartEmptyStateLiveData.value= EmptyState(true, R.string.emptyCart)
                   }

               }
           })
       }else{
           cartEmptyStateLiveData.value= EmptyState(true,R.string.emptyCartLogin,true)
       }

    }
    fun removeCart(cartItem: CartItem):Completable{
        return repository.removeCart(cartItem.cart_item_id).doAfterSuccess {
            val cartItemCount=EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
            cartItemCount?.let {
                it.count-=cartItem.count
                EventBus.getDefault().postSticky(it)
            }
        }.ignoreElement()
    }
    fun increaseCartCount(cartItem: CartItem):Completable{
        return repository.cartItemChangeCount(cartItem.cart_item_id,++cartItem.count).doAfterSuccess {
            var cartItemCount=EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
            cartItemCount.let {
                it.count+=1
                EventBus.getDefault().postSticky(it)
            }
            calculatePurchase()
        }.ignoreElement()
    }
    fun decreaseCartCount(cartItem: CartItem):Completable{
        return repository.cartItemChangeCount(cartItem.cart_item_id,--cartItem.count).doAfterSuccess {
            val cartItemCount=EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
            cartItemCount?.let {
                it.count-=1
                EventBus.getDefault().postSticky(it)
            }
            calculatePurchase()
        }.ignoreElement()
    }
    fun refreshCart()
    {
        getCart()
    }
    fun calculatePurchase()
    {
        var totalPrice=0
        var payablePrice=0
        var count=0
        cartItemsLiveData.value?.let {cartItems ->
            purchaseDetailsLiveData.value?.let {purchase ->
                cartItems.forEach{cartItem ->
                  payablePrice+=(cartItem.product.price-cartItem.product.discount)*cartItem.count
                    totalPrice+=cartItem.product.price*cartItem.count
                    count+=cartItem.count
                }
                purchase.payable_price=payablePrice
                purchase.total_price=totalPrice
                purchaseDetailsLiveData.postValue(purchase)
                if (count==0)
                {
                    cartEmptyStateLiveData.postValue(EmptyState(true,R.string.emptyCart,false))
                }
            }
        }
    }
}