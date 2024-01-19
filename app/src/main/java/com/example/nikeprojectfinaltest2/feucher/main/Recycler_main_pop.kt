package com.example.nikeprojectfinaltest2.feucher.main

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.data.productData
import com.example.nikeprojectfinaltest2.service.ImageLoadingService
import com.example.nikeprojectfinaltest2.utils.farsi
import com.example.nikeprojectfinaltest2.utils.implementSpringAnimationTrait
import com.example.nikeprojectfinaltest2.view.NikeImageView

class Recycler_main_pop(val imageLoadingService: ImageLoadingService):RecyclerView.Adapter<Recycler_main_pop.Recycler_pop_vh>() {
    var productClick:ProductPopClick?=null
    var productPopList=ArrayList<productData>()
        set(value) {
            field=value
            notifyDataSetChanged()
        }
    inner class Recycler_pop_vh(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val btn_favorite: ImageView =itemView.findViewById(R.id.img_favorite)
        val img_product_pop:NikeImageView=itemView.findViewById(R.id.img_product_pop)
        val txt_titlePop:TextView=itemView.findViewById(R.id.txt_title_pop)
        val txt_old_PricePop:TextView=itemView.findViewById(R.id.txt_old_price_pop)
        val txt_new_PricePop:TextView=itemView.findViewById(R.id.txt_new_price_pop)
        fun bindProduct(productData: productData)
        {
            btn_favorite.setImageResource(if (productData.isFavorite)R.drawable.baseline_favorite_fill else R.drawable.ic_favorites)
            btn_favorite.setOnClickListener({
                productClick?.productPopFavorite(productData)
                productData.isFavorite=!productData.isFavorite
                notifyItemChanged(adapterPosition)
            })
            imageLoadingService.load(img_product_pop,productData.image)
            txt_titlePop.setText(productData.title)
            txt_old_PricePop.paintFlags=Paint.STRIKE_THRU_TEXT_FLAG
            txt_old_PricePop.setText("${farsi(productData.previous_price.toString())}"+" "+"تومان")
            txt_new_PricePop.setText("${farsi(productData.price.toString())}"+" "+"تومان")
            itemView.implementSpringAnimationTrait()
            itemView.setOnClickListener({
                   productClick?.productPopItemClick(productData)
            })
        }
    }
    fun changeItem(productData: productData)
    {
        productData.isFavorite=!productData.isFavorite
        val index=productPopList.indexOf(productData)
        productPopList.set(index,productData)
        notifyItemChanged(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Recycler_pop_vh {
       return Recycler_pop_vh(LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_pop_product,parent,false))
    }

    override fun getItemCount(): Int {
      return  productPopList.size
    }

    override fun onBindViewHolder(holder: Recycler_pop_vh, position: Int) {
       holder.bindProduct(productPopList.get(position))
    }
    interface ProductPopClick{
        fun productPopItemClick(productData: productData)
        fun productPopFavorite(productData: productData)
    }
}