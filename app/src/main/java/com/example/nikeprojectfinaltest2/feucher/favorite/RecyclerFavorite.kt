package com.example.nikeprojectfinaltest2.feucher.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.data.productData
import com.example.nikeprojectfinaltest2.service.ImageLoadingService
import com.example.nikeprojectfinaltest2.view.NikeImageView

class RecyclerFavorite(val imageLoadingService: ImageLoadingService):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var productsFavorite=ArrayList<productData>()
    var onclick:OnclickItemFavorite?=null
    inner class RecyclerVhFavorite(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val txt_favoriteProduct:TextView=itemView.findViewById(R.id.txt_favoriteProduct)
        val img_favoriteProduct:NikeImageView=itemView.findViewById(R.id.img_favoriteProduct)
        val frameProductDelete:FrameLayout=itemView.findViewById(R.id.frame_deleteFavorite)
        fun bindProduct(product: productData)
        {
            txt_favoriteProduct.text=product.title
            imageLoadingService.load(img_favoriteProduct,product.image)
            itemView.setOnClickListener({
                onclick?.onclickItem(product)
            })
            frameProductDelete.setOnLongClickListener(object :View.OnLongClickListener{
                override fun onLongClick(v: View?): Boolean {
                    onclick?.onclickDeleteItem(product)
                    return false
                }
            })
        }
    }
    fun removeItemFromFavorite(product: productData)
    {
        val index=productsFavorite.indexOf(product)
        if (index>-1)
        {
            productsFavorite.removeAt(index)
            notifyItemRemoved(index)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecyclerVhFavorite(LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_favorite,parent,false))
    }

    override fun getItemCount(): Int {
       return productsFavorite.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       if (holder is RecyclerVhFavorite)
       {
           return holder.bindProduct(productsFavorite.get(position))
       }
    }
    interface OnclickItemFavorite
    {
        fun onclickItem(product: productData)
        fun onclickDeleteItem(product: productData)
    }
}