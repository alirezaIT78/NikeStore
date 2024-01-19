package com.example.nikeprojectfinaltest2.feucher.product

import android.os.Build
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.example.nikeprojectfinaltest.data.CartItem
import com.example.nikeprojectfinaltest.data.CartListResponse
import com.example.nikeprojectfinaltest2.common.NikeCompletable
import com.example.nikeprojectfinaltest2.common.baseViewModel
import com.example.nikeprojectfinaltest2.common.myObserver
import com.example.nikeprojectfinaltest2.data.Comment
import com.example.nikeprojectfinaltest2.data.TokenContainer
import com.example.nikeprojectfinaltest2.data.productData
import com.example.nikeprojectfinaltest2.repo.CartRepository
import com.example.nikeprojectfinaltest2.repo.CommentRepository
import com.example.nikeprojectfinaltest2.repo.ProductRepository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductViewModel(bundle: Bundle,val commentRepository: CommentRepository,val cartRepository:CartRepository,val productRepository: ProductRepository):baseViewModel() {

    val productLiveDataDetails=MutableLiveData<productData>()
    val commentLiveData=MutableLiveData<List<Comment>>()
    val cartItemCount=MutableLiveData<Int>()
    init {
        progressLiveData.value=true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            productLiveDataDetails.value=bundle.getParcelable("data",productData::class.java) as productData
        }
        else{
          productLiveDataDetails.value= bundle.getParcelable("data")
        }
        getComment()
    }
    fun getComment(){
        commentRepository.getComment(productLiveDataDetails.value!!.id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).doFinally { progressLiveData.value=false }.subscribe(object :myObserver<List<Comment>>(compositeDisposable){
                override fun onSuccess(t: List<Comment>) {
                    commentLiveData.value=t
                    btnClickTest(productLiveDataDetails.value!!.id)
                }

            })
    }
    fun onClickBtnAddToCart():Completable=cartRepository.addToCart(productLiveDataDetails.value!!.id).doAfterSuccess {
      btnClickTest(productLiveDataDetails.value!!.id)
    }.ignoreElement()
    fun addOrRemoveFavorite(productData: productData)
    {
        if (productData.isFavorite)
            productRepository.deleteFavoriteProduct(productData).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(object : NikeCompletable(compositeDisposable){
                    override fun onComplete() {
                        productData.isFavorite=false
                    }
                })else{
            productRepository.addProductToFavorite(productData).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io()).subscribe(object : NikeCompletable(compositeDisposable){
                    override fun onComplete() {
                        productData.isFavorite=true
                    }
                })
        }
    }
    fun btnClickTest(productID:Int)
    {
        if (!TokenContainer.accessToken.isNullOrEmpty())
        {
            var cartCount:Int=0
            cartRepository.getCart().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doFinally {
            }.subscribe(object :myObserver<CartListResponse>(compositeDisposable)
            {
                override fun onSuccess(t: CartListResponse) {
                    t.cart_items.forEach {cartItem ->
                        if (cartItem.product.id.equals(productID))
                        {
                            cartCount=cartItem.count
                            cartItemCount.value=cartCount
                        }
                    }
                }
            })
        }

    }
}