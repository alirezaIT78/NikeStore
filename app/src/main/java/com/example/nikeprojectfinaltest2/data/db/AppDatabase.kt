package com.example.nikeprojectfinaltest2.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nikeprojectfinaltest2.data.productData
import com.example.nikeprojectfinaltest2.source.productLocalDataSource

@Database(entities = [productData::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getDao():productLocalDataSource
}