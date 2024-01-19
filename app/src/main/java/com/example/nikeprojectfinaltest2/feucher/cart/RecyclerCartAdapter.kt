package com.example.nikeprojectfinaltest2.feucher.cart

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeprojectfinaltest.data.CartItem
import com.example.nikeprojectfinaltest.data.Purchase
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.service.ImageLoadingService
import com.example.nikeprojectfinaltest2.utils.farsi
import com.example.nikeprojectfinaltest2.view.NikeImageView
const val ViewTypeCartItem=0
const val ViewTypeCartPurchase=1
var errorMustCartItem=""
class RecyclerCartAdapter(var viewType: Int,val imageLoadingService: ImageLoadingService):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onclickItemCart:onClickItemCart?=null
    var cartItems=ArrayList<CartItem>()
    var purchase:Purchase?=null
    inner class RecyclerVh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img_productCart: NikeImageView = itemView.findViewById(R.id.img_productCart)
        val txt_titleProductCart: TextView = itemView.findViewById(R.id.txt_productTitle_cart)
        val txt_count: TextView = itemView.findViewById(R.id.txt_count)
        val btn_increase: ImageView = itemView.findViewById(R.id.img_increase)
        val btn_decrease: ImageView = itemView.findViewById(R.id.img_decrease)
        val txt_oldPriceCart: TextView = itemView.findViewById(R.id.old_price_cart)
        val txt_newPriceCart: TextView = itemView.findViewById(R.id.new_price_cart)
        val frameDelete: FrameLayout = itemView.findViewById(R.id.frame_delete)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progress_cartItemCount)
        fun bindCartItem(cartItem: CartItem) {
            if (cartItem.count<2)
            {
                btn_decrease.setImageResource(R.drawable.baseline_delete_outline_24)
            }
            progressBar.visibility=if (cartItem.progressBarCartItem ==true)View.VISIBLE else View.GONE
            txt_count.visibility=if (cartItem.progressBarCartItem==true)View.INVISIBLE else View.VISIBLE
            imageLoadingService.load(img_productCart, cartItem.product.image)
            progressBar.visibility = View.GONE
            img_productCart.setOnClickListener({
                onclickItemCart?.clickedOnImgProductCart(cartItem)
            })
            txt_titleProductCart.text = cartItem.product.title
            txt_count.setText("${farsi(cartItem.count.toString())}")
            txt_newPriceCart.setText("${farsi((cartItem.product.price - cartItem.product.discount).toString())}" + " " + "تومان")
            txt_oldPriceCart.setText("${farsi(cartItem.product.price.toString())}" + " " + "تومان")
            btn_increase.setOnClickListener({
                if (cartItem.count<5) {
                    progressBar.visibility = View.VISIBLE
                    cartItem.progressBarCartItem = true
                    txt_count.visibility = View.INVISIBLE
                    onclickItemCart?.clickedOnIncreaseBtn(cartItem)
                    errorMustCartItem = ""
                }
                else{
                    errorMustCartItem="تعداد سفارش این محصول نباید بیشتر از ۵ باشد"
                    onclickItemCart?.clickedOnIncreaseBtn(cartItem)
                }
            })
            btn_decrease.setOnClickListener({
                if (cartItem.count > 1) {
                    progressBar.visibility = View.VISIBLE
                    cartItem.progressBarCartItem = true
                    txt_count.visibility = View.INVISIBLE
                    onclickItemCart?.clickedOnDecreaseBtn(cartItem)
                }
                else
                {
                    onclickItemCart?.clickedOnDeleteBtn(cartItem)
                }
            })
            frameDelete.setOnClickListener({
                onclickItemCart?.clickedOnDeleteBtn(cartItem)
            })
        }

    }
    class purchaseVh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_totalPrice: TextView = itemView.findViewById(R.id.txt_totalPrice)
        val txt_shippingConst: TextView = itemView.findViewById(R.id.txt_shippingPrice)
        val txt_payablePrice: TextView = itemView.findViewById(R.id.txt_payablePrice)
        @SuppressLint("SuspiciousIndentation")
        fun bindPurchase(totalPrice: Int, shippingConst: Int, payablePrice: Int) {
            txt_totalPrice.setText("${farsi(totalPrice.toString())}"+" "+"تومان")
            if (shippingConst==0)
            txt_shippingConst.setText("رایگان")
            else
            txt_shippingConst.setText("${farsi(shippingConst.toString())}"+" "+"تومان")
            txt_payablePrice.setText("${farsi(payablePrice.toString())}"+" "+"تومان")
        }
    }
  interface onClickItemCart{
      fun clickedOnIncreaseBtn(cartItem: CartItem)
      fun clickedOnDecreaseBtn(cartItem: CartItem)
      fun clickedOnImgProductCart(cartItem: CartItem)
      fun clickedOnDeleteBtn(cartItem: CartItem)
  }

    override fun getItemViewType(position: Int): Int {
        if (position==cartItems.size)
        {
            return ViewTypeCartPurchase
        }
        else {
            return ViewTypeCartItem
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType== ViewTypeCartItem)
       return RecyclerVh(LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_cart,parent,false))
        else
            return purchaseVh(LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_cart_purchase,parent,false))

    }

    override fun getItemCount(): Int {
       return cartItems.size+1
    }
    fun removeCartItem(cartItem: CartItem)
    {
        val index=cartItems.indexOf(cartItem)
        if (index>-1)
        {
            cartItems.remove(cartItem)
            notifyItemRemoved(index)
        }
    }
    fun increaseCartItem(cartItem: CartItem):Int
    {
        val index=cartItems.indexOf(cartItem)
        cartItem.progressBarCartItem=false
        return index
    }
    fun decreaseCartItem(cartItem: CartItem):Int
    {
        val index=cartItems.indexOf(cartItem)
        cartItem.progressBarCartItem=false
        return index
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       if (holder is RecyclerVh)
       {
           holder.bindCartItem(cartItems.get(position))
       }else if (holder is purchaseVh)
       {
           purchase?.let {
               holder.bindPurchase(it.total_price,it.shipping_cost,it.payable_price)
           }
       }
    }
}