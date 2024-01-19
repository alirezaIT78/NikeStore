package com.example.nikeprojectfinaltest2.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import androidx.room.Room
import com.example.nikeprojectfinaltest2.data.db.AppDatabase
import com.example.nikeprojectfinaltest2.feucher.auth.viewModelUser
import com.example.nikeprojectfinaltest2.feucher.cart.RecyclerCartAdapter
import com.example.nikeprojectfinaltest2.feucher.cart.ViewModelCart
import com.example.nikeprojectfinaltest2.feucher.checkout.ViewModelCheckout
import com.example.nikeprojectfinaltest2.feucher.comment.viewModelComment
import com.example.nikeprojectfinaltest2.feucher.favorite.FavoriteViewModel
import com.example.nikeprojectfinaltest2.feucher.favorite.RecyclerFavorite
import com.example.nikeprojectfinaltest2.feucher.list.viewModelProductList
import com.example.nikeprojectfinaltest2.feucher.main.RecyclerMain_latest_product
import com.example.nikeprojectfinaltest2.feucher.main.Recycler_main_pop
import com.example.nikeprojectfinaltest2.feucher.main.ViewModelActivityMain
import com.example.nikeprojectfinaltest2.feucher.main.mainViewModel
import com.example.nikeprojectfinaltest2.feucher.order.OrderHistoryViewModel
import com.example.nikeprojectfinaltest2.feucher.product.ProductViewModel
import com.example.nikeprojectfinaltest2.feucher.product.RecyclerComment
import com.example.nikeprojectfinaltest2.feucher.profile.ProfileViewModel
import com.example.nikeprojectfinaltest2.feucher.shipping.ShippingViewModel
import com.example.nikeprojectfinaltest2.repo.BannerRepository
import com.example.nikeprojectfinaltest2.repo.BannerRepositoryImpel
import com.example.nikeprojectfinaltest2.repo.CartRepository
import com.example.nikeprojectfinaltest2.repo.CartRepositoryImpl
import com.example.nikeprojectfinaltest2.repo.CommentRepository
import com.example.nikeprojectfinaltest2.repo.CommentRepositoryImpl
import com.example.nikeprojectfinaltest2.repo.OrderRepository
import com.example.nikeprojectfinaltest2.repo.OrderRepositoryImpl
import com.example.nikeprojectfinaltest2.repo.ProductRepository
import com.example.nikeprojectfinaltest2.repo.ProductRepositoryImpl
import com.example.nikeprojectfinaltest2.repo.UserRepository
import com.example.nikeprojectfinaltest2.repo.UserRepositoryImpl
import com.example.nikeprojectfinaltest2.service.ApiService
import com.example.nikeprojectfinaltest2.service.ImageLoadingService
import com.example.nikeprojectfinaltest2.service.RetrofitApiService
import com.example.nikeprojectfinaltest2.service.imageLoadingServiceImpl
import com.example.nikeprojectfinaltest2.source.BannerRemoteDataSource
import com.example.nikeprojectfinaltest2.source.CartRemoteDataSource
import com.example.nikeprojectfinaltest2.source.CommentRemoteDataSource
import com.example.nikeprojectfinaltest2.source.OrderRemoteDataSource
import com.example.nikeprojectfinaltest2.source.UserLocalDataSource
import com.example.nikeprojectfinaltest2.source.UserRemoteDataSource
import com.example.nikeprojectfinaltest2.source.productLocalDataSource
import com.example.nikeprojectfinaltest2.source.productRemoteDataSource
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import kotlin.math.sin

class App:Application() {
    @SuppressLint("CommitPrefEdits")
    override fun onCreate() {
        Fresco.initialize(this)
        val module= module {
            single<ImageLoadingService> {imageLoadingServiceImpl()}
            single <ApiService>{ RetrofitApiService() }
            factory {(viewType:Int)-> RecyclerMain_latest_product(viewType,get()) }
            factory<CartRepository>{ CartRepositoryImpl(CartRemoteDataSource(get()))  }
            factory { Recycler_main_pop(get()) }
            single { UserLocalDataSource(get()) }
            single <SharedPreferences>{this@App.getSharedPreferences("app_setting", MODE_PRIVATE)  }
            single <UserRepository>{UserRepositoryImpl(UserLocalDataSource(get()),UserRemoteDataSource(get()))  }
            single { Room.databaseBuilder(this@App,AppDatabase::class.java,"app_db").build() }
            factory<ProductRepository> {ProductRepositoryImpl(productRemoteDataSource(get()),get<AppDatabase>().getDao()) }
            factory <CommentRepository>{ CommentRepositoryImpl(CommentRemoteDataSource(get())) }
            factory <BannerRepository>{BannerRepositoryImpel(BannerRemoteDataSource(get()))  }
            factory { RecyclerComment() }
            factory { (viewTypes:Int)->RecyclerCartAdapter(viewTypes,get()) }
            single<OrderRepository> { OrderRepositoryImpl(OrderRemoteDataSource(get())) }
            single { RecyclerFavorite(get()) }
            viewModel { (bundle:Bundle)->ProductViewModel(bundle,get(),get(),get())}
            viewModel{ mainViewModel(get(),get()) }
            viewModel {(productID:Int)-> viewModelComment(productID,get()) }
            viewModel {(sort:Int)-> viewModelProductList(sort,get()) }
            viewModel { viewModelUser(get()) }
            viewModel { ViewModelCart(get()) }
            viewModel { ViewModelActivityMain(get()) }
            viewModel { ShippingViewModel(get()) }
            viewModel { (orderId:Int)->ViewModelCheckout(orderId,get()) }
            viewModel {ProfileViewModel(get(),get())}
            viewModel { FavoriteViewModel(get()) }
            viewModel { OrderHistoryViewModel(get()) }
        }
        startKoin {
            androidContext(this@App)
            modules(module)
        }
       val userRepository:UserRepository=get()
        userRepository.loadToken()
        //val sharedPreferences:SharedPreferences=get()
      //  sharedPreferences.edit().clear()
        super.onCreate()
    }
}