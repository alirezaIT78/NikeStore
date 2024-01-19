package com.example.nikeprojectfinaltest2.feucher.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.common.NikeCompletable
import com.example.nikeprojectfinaltest2.common.baseActivity
import com.example.nikeprojectfinaltest2.data.productData
import com.example.nikeprojectfinaltest2.feucher.product.ProductDetailsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteActivity : baseActivity(), RecyclerFavorite.OnclickItemFavorite {
    val viewModel:FavoriteViewModel by viewModel()
    val recyclerFavorite:RecyclerFavorite by inject()
    val compositeDisposable=CompositeDisposable()
    var recyclerView:RecyclerView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        val btn_info:ImageView=findViewById(R.id.btn_info)
        val btnBack:ImageView=findViewById(R.id.btn_back_favorite)
        btn_info.setOnClickListener({
            showSnackBar(getString(R.string.info_favorite))
        })
        viewModel.progressLiveData.observe(this){
            setProgress(it)
        }
        btnBack.setOnClickListener({
            finish()
        })
        recyclerFavorite.onclick=this
        recyclerView=findViewById(R.id.recycler_favorite)
        recyclerView?.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        viewModel.favoriteProductLiveData.observe(this){
            if (!it.isNullOrEmpty()) {
                recyclerFavorite.productsFavorite = it as ArrayList<productData>
                recyclerView?.adapter = recyclerFavorite
            }else{
                recyclerFavorite.productsFavorite.clear()
                recyclerView?.adapter=recyclerFavorite
                setEmptyState(R.layout.empty_layout_favorite)
            }
        }
    }

    override fun onclickItem(product: productData) {
       val intent= Intent(this,ProductDetailsActivity::class.java)
        intent.putExtra("data",product)
        startActivity(intent)
    }

    override fun onclickDeleteItem(product: productData) {
        viewModel.removeFavorite(product).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikeCompletable(compositeDisposable)
            {
                override fun onComplete() {
                    recyclerFavorite.removeItemFromFavorite(product)
                    if (recyclerFavorite.productsFavorite.size==0)
                        setEmptyState(R.layout.empty_layout_favorite)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteProduct()
    }
}