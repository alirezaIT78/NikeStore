package com.example.nikeprojectfinaltest2.feucher.list

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.common.baseActivity
import com.example.nikeprojectfinaltest2.data.productData
import com.example.nikeprojectfinaltest2.feucher.main.RecyclerMain_latest_product
import com.example.nikeprojectfinaltest2.feucher.main.mainViewModel
import com.example.nikeprojectfinaltest2.feucher.main.viewTypeBig
import com.example.nikeprojectfinaltest2.feucher.main.viewTypeSmall
import com.example.nikeprojectfinaltest2.feucher.product.ProductDetailsActivity
import com.example.nikeprojectfinaltest2.feucher.product.deleteOrAddItemFavorite
import com.example.nikeprojectfinaltest2.feucher.product.getProduct
import com.example.nikeprojectfinaltest2.feucher.product.onClickBtnFavoriteOfProductList
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProductListActivity : baseActivity(),RecyclerMain_latest_product.productClick {
    val viewModel_productList:viewModelProductList by viewModel { parametersOf(intent.extras!!.getInt("data"))  }
    val main_viewModel:mainViewModel by inject()
    val recyclerViewProductAdapter:RecyclerMain_latest_product by inject { parametersOf(viewTypeSmall) }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        val viewToolbar: View =findViewById(R.id.viewToolbar)
        val recyclerViewProduct:RecyclerView=findViewById(R.id.recycler_productList)
        val sorted=intent.getIntExtra("data",0)
        val txt_sortedBy:TextView=findViewById(R.id.txt_sortBy)
        if (sorted==0)
            txt_sortedBy.text="جدید ترین"
        if (sorted==1)
            txt_sortedBy.text="محبوب ترین"
        val btnBackProductList:ImageView=findViewById(R.id.btn_backProductList)
        btnBackProductList.setOnClickListener({
            finish()
        })
        val gridView=GridLayoutManager(this,2)
        recyclerViewProduct.layoutManager=gridView
        viewModel_productList.productListLiveData.observe(this,{
            recyclerViewProductAdapter.productLatestList=it as ArrayList<productData>
            recyclerViewProduct.adapter=recyclerViewProductAdapter
        })
        recyclerViewProductAdapter.productclick=this
        viewToolbar.setOnClickListener({
            val dialog=MaterialAlertDialogBuilder(this).setSingleChoiceItems(R.array.arraySort,viewModel_productList.sort
            ) {
                    dialog, selectedItemIndex ->
                viewModel_productList.onSelectedChangeByUser(selectedItemIndex)
                dialog.dismiss()
            }.setTitle(getString(R.string.sort))
            dialog.show()
        })
        viewModel_productList.progressLiveData.observe(this,{
            setProgress(it)
        })
        viewModel_productList.sortLiveData.observe(this,{
            txt_sortedBy.text=getString(it)
        })
        val btnGrid:ImageView=findViewById(R.id.img_grid)
        btnGrid.setOnClickListener({
            if (recyclerViewProductAdapter.viewType== viewTypeSmall)
            {
                gridView.spanCount=1
                recyclerViewProductAdapter.viewType= viewTypeBig
                btnGrid.setImageResource(R.drawable.ic_plus_square)
            }else{
                gridView.spanCount=2
                recyclerViewProductAdapter.viewType= viewTypeSmall
                btnGrid.setImageResource(R.drawable.ic_grid)
            }
        })
    }

    override fun onClickProduct(productData: productData) {
        val intent=Intent(this,ProductDetailsActivity::class.java)
        intent.putExtra("data",productData)
        onClickBtnFavoriteOfProductList=true
        startActivity(intent)
    }

    override fun onClickFavoriteProduct(productData: productData) {
        main_viewModel.addOrRemoveFavorite(productData)
        deleteOrAddItemFavorite=true
    }

    override fun onResume() {
        super.onResume()
        if (onClickBtnFavoriteOfProductList)
        {
            recyclerViewProductAdapter.changeItem(getProduct!!)
            onClickBtnFavoriteOfProductList=false
        }
    }
}