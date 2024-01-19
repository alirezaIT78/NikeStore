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
const val viewTypeRounded:Int=0
const val viewTypeSmall:Int=1
const val viewTypeBig:Int=2
class RecyclerMain_latest_product(var viewType:Int,val imageLoadingService: ImageLoadingService):RecyclerView.Adapter<RecyclerMain_latest_product.Recycler_latest_Vh>() {
    var productclick:productClick?=null
    var productLatestList=ArrayList<productData>()
        set(value) {
            field=value
            notifyDataSetChanged()
        }
    inner class Recycler_latest_Vh(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val btn_favorite:ImageView=itemView.findViewById(R.id.img_favorite)
       val txt_title_latest:TextView=itemView.findViewById(R.id.txt_title_latest)
       val txt_oldPrice_latest:TextView=itemView.findViewById(R.id.txt_old_price_latest)
       val txt_new_latest:TextView=itemView.findViewById(R.id.txt_new_price_latest)
        val img_latest_product:NikeImageView=itemView.findViewById(R.id.img_product_latest)
        fun bindProductLatest(productData: productData)
        {
            btn_favorite.setImageResource(if (productData.isFavorite)R.drawable.baseline_favorite_fill else R.drawable.ic_favorites)
            btn_favorite.setOnClickListener({
                productclick?.onClickFavoriteProduct(productData)
                productData.isFavorite=!productData.isFavorite
                notifyItemChanged(adapterPosition)
            })
            imageLoadingService.load(img_latest_product,productData.image)
            txt_title_latest.setText(productData.title)
            txt_oldPrice_latest.paintFlags=Paint.STRIKE_THRU_TEXT_FLAG
            txt_oldPrice_latest.setText("${farsi(productData.previous_price.toString())}"+" "+"تومان")
            txt_new_latest.setText("${farsi(productData.price.toString())}"+" "+"تومان")
            itemView.implementSpringAnimationTrait()
            itemView.setOnClickListener({
                 productclick?.onClickProduct(productData)
            })
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Recycler_latest_Vh {
        val ResId=when(viewType)
        {
            viewTypeRounded -> R.layout.item_recycler_latest_product
            viewTypeBig->R.layout.item_recycler_big_product
            viewTypeSmall->R.layout.item_recycler_small_product
            else ->throw IllegalStateException("ResIdMustBeOneOfTheItems")
        }
        return Recycler_latest_Vh(LayoutInflater.from(parent.context).inflate(ResId,parent,false))
    }
    fun changeItem(productData: productData)
    {
        val index=productLatestList.indexOf(productData)
        productLatestList.set(index,productData)
        notifyItemChanged(index)
    }
    override fun getItemCount(): Int {
        return productLatestList.size
    }

    override fun onBindViewHolder(holder: Recycler_latest_Vh, position: Int) {
        holder.bindProductLatest(productLatestList.get(position))
    }
    interface productClick{
        fun onClickProduct(productData: productData)
        fun onClickFavoriteProduct(productData: productData)
    }
}