package com.example.nikeprojectfinaltest2.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nikeprojectfinaltest2.data.productData
import io.reactivex.Completable
import io.reactivex.Single
@Dao
interface productLocalDataSource:ProductDataSource {
    override fun getProductFromApi(sort:Int): Single<List<productData>> {
        TODO("Not yet implemented")
    }
    @Query("SELECT * FROM tbl_favorite")
    override fun getFavoriteProductList(): Single<List<productData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun addProductToFavorite(product: productData): Completable

    @Delete
    override fun deleteFavoriteProduct(product: productData): Completable
}