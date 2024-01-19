package com.example.nikeprojectfinaltest2.feucher.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeprojectfinaltest.data.CartItem
import com.example.nikeprojectfinaltest.data.Purchase
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.common.AuthStart
import com.example.nikeprojectfinaltest2.common.NikeCompletable
import com.example.nikeprojectfinaltest2.common.baseFragment
import com.example.nikeprojectfinaltest2.feucher.auth.AuthActivity
import com.example.nikeprojectfinaltest2.feucher.checkout.checkoutBtnClick
import com.example.nikeprojectfinaltest2.feucher.product.ProductDetailsActivity
import com.example.nikeprojectfinaltest2.feucher.shipping.ShippingActivity
import com.google.android.material.button.MaterialButton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
var BannerCartCount=0
var purchaseGlobal:Purchase?=null
class FragmentCart:baseFragment(), RecyclerCartAdapter.onClickItemCart {
    var btnPurchase:MaterialButton?=null
    val viewModel:ViewModelCart by inject()
    var recyclerView:RecyclerView?=null
    val recyclerCartAdapter:RecyclerCartAdapter by inject { parametersOf(ViewTypeCartItem) }
    val compositeDisposable=CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=layoutInflater.inflate(R.layout.fragment_cart,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView=view.findViewById(R.id.recycler_cart)
        recyclerCartAdapter.onclickItemCart=this
        btnPurchase=view.findViewById(R.id.btn_purchase)
        recyclerView?.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        viewModel.progressLiveData.observe(viewLifecycleOwner){
            setProgress(it)
        }
        viewModel.cartItemsLiveData.observe(viewLifecycleOwner){
           recyclerCartAdapter.cartItems=it as ArrayList<CartItem>
            recyclerView?.adapter=recyclerCartAdapter
            btnPurchase?.visibility=View.VISIBLE
        }
        viewModel.purchaseDetailsLiveData.observe(viewLifecycleOwner){
            recyclerCartAdapter.purchase=it
            purchaseGlobal=it
            recyclerView?.adapter=recyclerCartAdapter
        }
        viewModel.cartEmptyStateLiveData.observe(viewLifecycleOwner){
            val emptyState=setEmptyState(R.layout.empty_state_cart)
            if (it.mustShow)
            {
                emptyState?.let {myEmptystate->
                    val imgEmptyState:ImageView=view.findViewById(R.id.imgEmptyState)
                    val txt_emptyState:TextView= myEmptystate.findViewById(R.id.txt_emptyCart)
                    val btn_LoginEmptyState:MaterialButton=myEmptystate.findViewById(R.id.btn_emptyCartLogin)
                    if (it.mustShowBtnLogin) {
                        imgEmptyState.setImageResource(R.drawable.empty_cart_login)
                        btn_LoginEmptyState.visibility = View.VISIBLE
                    }
                    else{
                        imgEmptyState.setImageResource(R.drawable.empty_cart2)
                        btn_LoginEmptyState.visibility=View.GONE
                    }
                    txt_emptyState.text=getString(it.resIdString)
                    btn_LoginEmptyState.setOnClickListener({
                        AuthStart=1
                        startActivity(Intent(requireContext(),AuthActivity::class.java))
                    })
                }

            }else{
                emptyState?.visibility=View.GONE
            }
        }

        btnPurchase?.setOnClickListener({
            val intent=Intent(requireContext(),ShippingActivity()::class.java)
            intent.putExtra("data",viewModel.purchaseDetailsLiveData.value)
            checkoutBtnClick=false
            startActivity(intent)
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshCart()
    }

    override fun clickedOnIncreaseBtn(cartItem: CartItem) {
        if (!errorMustCartItem.isNullOrEmpty())
        {
            showSnackBar(errorMustCartItem)
            errorMustCartItem=""
        }
        else{
            viewModel.increaseCartCount(cartItem).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(object :NikeCompletable(compositeDisposable)
                {
                    override fun onComplete() {

                        val index= recyclerCartAdapter.increaseCartItem(cartItem)
                       recyclerView?.scrollToPosition(index)
                    }
                })
        }
    }

    override fun clickedOnDecreaseBtn(cartItem: CartItem) {
        viewModel.decreaseCartCount(cartItem).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object :NikeCompletable(compositeDisposable){
                @SuppressLint("SuspiciousIndentation")
                override fun onComplete() {
                  val index=recyclerCartAdapter.decreaseCartItem(cartItem)
                    recyclerView?.scrollToPosition(index)
                }
            })
    }

    override fun clickedOnImgProductCart(cartItem: CartItem) {
        val intent= Intent(context,ProductDetailsActivity::class.java)
        val oldPrice:Int=cartItem.product.price
        val newPrice:Int=cartItem.product.price-cartItem.product.discount
        cartItem.product.previous_price=oldPrice
        cartItem.product.price=newPrice
        BannerCartCount=1
        intent.putExtra("data",cartItem.product)
        startActivity(intent)
    }

    override fun clickedOnDeleteBtn(cartItem: CartItem) {
       viewModel.removeCart(cartItem).subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread()).subscribe(object :NikeCompletable(compositeDisposable){
               override fun onComplete() {
                   recyclerCartAdapter.removeCartItem(cartItem)
                   viewModel.calculatePurchase()
               }
           })
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        btnPurchase?.visibility=View.GONE
    }
}