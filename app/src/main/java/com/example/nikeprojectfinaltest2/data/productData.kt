package com.example.nikeprojectfinaltest2.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
@Entity(tableName = "tbl_favorite")
@Parcelize
data class productData(
    val discount: Int,
@PrimaryKey(autoGenerate = false)
    val id: Int,
    val image: String,
    var previous_price: Int,
    var price: Int,
    val status: Int,
    val title: String
):Parcelable{
  var isFavorite:Boolean=false
}
const val sortByLatestProduct=0
const val sortByPopularProduct=1
const val sortByDesc=2
const val sortByAcs=3