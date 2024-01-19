package com.example.nikeprojectfinaltest2.service


import android.annotation.SuppressLint
import com.example.nikeprojectfinaltest.data.AddToCartResource
import com.example.nikeprojectfinaltest.data.CartItemCount
import com.example.nikeprojectfinaltest.data.CartListResponse
import com.example.nikeprojectfinaltest.data.Checkout
import com.example.nikeprojectfinaltest.data.MessageResponse
import com.example.nikeprojectfinaltest.data.OrderHistoryItem
import com.example.nikeprojectfinaltest.data.SubmitOrderRequest
import com.example.nikeprojectfinaltest2.data.Banner
import com.example.nikeprojectfinaltest2.data.Comment
import com.example.nikeprojectfinaltest2.data.TokenContainer
import com.example.nikeprojectfinaltest2.data.TokenResponse
import com.example.nikeprojectfinaltest2.data.productData
import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("product/list")
    fun getListOfProduct(@Query("sort")sort:String):Single<List<productData>>
    @GET("banner/slider")
    fun getBanner():Single<List<Banner>>
    @GET("comment/list")
    fun getComment(@Query("product_id")product_id:Int):Single<List<Comment>>
    @POST("comment/add")
    fun addComment(@Body jsonObject: JsonObject):Completable
    @POST("cart/add")
    fun addToCart(@Body jsonObject: JsonObject):Single<AddToCartResource>
    @GET("cart/list")
    fun getCart():Single<CartListResponse>
    @POST("cart/remove")
    fun removeCart(@Body jsonObject: JsonObject):Single<MessageResponse>
    @POST("cart/changeCount")
    fun changeCartCount(@Body jsonObject: JsonObject):Single<AddToCartResource>
    @GET ("cart/count")
    fun getCountCart():Single<CartItemCount>
    @POST("auth/token")
    fun login(@Body jsonObject: JsonObject):Single<TokenResponse>
    @POST("user/register")
    fun singUp(@Body jsonObject: JsonObject):Single<MessageResponse>
    @POST("auth/token")
    fun refreshToken(@Body jsonObject: JsonObject):Call<TokenResponse>
    @POST("order/submit")
    fun submitOrder(@Body jsonObject: JsonObject):Single<SubmitOrderRequest>
    @GET("order/checkout")
    fun orderCheckOut(@Query("order_id")orderID:Int):Single<Checkout>
    @GET("order/list")
    fun getOrders():Single<List<OrderHistoryItem>>
}
@SuppressLint("SuspiciousIndentation")
fun RetrofitApiService():ApiService{
    val okHttpClient=OkHttpClient.Builder()
        .addInterceptor {
            val oldRequest=it.request()
            val newRequest=oldRequest.newBuilder()
                if (TokenContainer.accessToken !=null)
                    newRequest.addHeader("Authorization","Bearer ${TokenContainer.accessToken}")

            newRequest.addHeader("Accept","application/json")
            newRequest.method(oldRequest.method, oldRequest.body)
            return@addInterceptor it.proceed(newRequest.build())
        }.addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
        .build()
   val retrofit:Retrofit=Retrofit.Builder().baseUrl("http://expertdevelopers.ir/api/v1/").addConverterFactory(GsonConverterFactory.create())
       .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
       .client(okHttpClient)
       .build()
    return retrofit.create(ApiService::class.java)
}