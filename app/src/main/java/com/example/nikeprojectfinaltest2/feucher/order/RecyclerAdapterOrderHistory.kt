package com.example.nikeprojectfinaltest2.feucher.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeprojectfinaltest.data.OrderHistoryItem
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.utils.convertDpToPixel
import com.example.nikeprojectfinaltest2.utils.farsi
import com.example.nikeprojectfinaltest2.view.NikeImageView

class RecyclerAdapterOrderHistory(val context:Context,val orderItems: List<OrderHistoryItem>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val imageLayoutParams:LinearLayout.LayoutParams
    init {
        val size= convertDpToPixel(100f,context).toInt()
        val margin= convertDpToPixel(16f,context).toInt()
        imageLayoutParams=LinearLayout.LayoutParams(size,size)
        imageLayoutParams.setMargins(margin,0,margin,0)
    }
    inner class RecyclerOrderHistoryVh(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val txt_paymentAmount:TextView=itemView.findViewById(R.id.txt_paymentAmount)
        val txt_orderId:TextView=itemView.findViewById(R.id.txt_orderId)
        val linearLayout:LinearLayout=itemView.findViewById(R.id.orderHistoryLL)
        fun bindOrder(orderHistoryItem: OrderHistoryItem){
            linearLayout.removeAllViews()
            txt_orderId.setText("${farsi(orderHistoryItem.id.toString())}")
            txt_paymentAmount.setText("${farsi(orderHistoryItem.payable.toString())}"+" "+"تومان")
            orderHistoryItem.order_items.forEach {
                val imageView=NikeImageView(context)
                imageView.layoutParams=imageLayoutParams
                imageView.setImageURI(it.product.image)
                linearLayout.addView(imageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecyclerOrderHistoryVh(LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_order_history,parent,false))
    }

    override fun getItemCount(): Int {
        return orderItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecyclerOrderHistoryVh)
        {
            holder.bindOrder(orderItems.get(position))
        }
    }
}