package com.example.nikeprojectfinaltest2.feucher.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Filter
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.common.baseFragment
import com.example.nikeprojectfinaltest2.common.errorConnect
import com.example.nikeprojectfinaltest2.data.productData
import com.example.nikeprojectfinaltest2.data.sortByLatestProduct
import com.example.nikeprojectfinaltest2.data.sortByPopularProduct
import com.example.nikeprojectfinaltest2.feucher.list.ProductListActivity
import com.example.nikeprojectfinaltest2.feucher.product.ProductDetailsActivity
import com.example.nikeprojectfinaltest2.feucher.product.bannerCountLoad
import com.example.nikeprojectfinaltest2.feucher.product.deleteOrAddItemFavorite
import com.example.nikeprojectfinaltest2.utils.convertDpToPixel
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf

class FragmentMain : baseFragment(),RecyclerMain_latest_product.productClick,Recycler_main_pop.ProductPopClick{
    var productBanner1:productData?=null
    var productBanner2:productData?=null
    var productBanner3:productData?=null
    val mainViewModel: mainViewModel by viewModel()
    var recyclerSearch:RecyclerView?=null
    val oldProducts=ArrayList<productData>()
    val recyclerAdapterLatest:RecyclerMain_latest_product by inject{ parametersOf(viewTypeRounded) }
    val recyclerAdapterPop:Recycler_main_pop by inject()
    fun Filter(userSearch:String,productList:ArrayList<productData>){
       val productsSearch=ArrayList<productData>()
        if (userSearch.isEmpty())
        {
            productsSearch.clear()
            recyclerAdapterLatest.productLatestList.clear()
            recyclerAdapterLatest.notifyDataSetChanged()
            recyclerAdapterLatest.productLatestList.addAll(oldProducts)
            recyclerSearch?.visibility=View.GONE
        }else{
            productList.forEach{
                if (it.title.lowercase().contains(userSearch.lowercase()))
                {
                    productsSearch.add(it)
                }
            }
            recyclerAdapterLatest.productLatestList.clear()
            recyclerAdapterLatest.notifyDataSetChanged()
            recyclerAdapterLatest.productLatestList.addAll(productsSearch)
            recyclerSearch?.adapter=recyclerAdapterLatest
            recyclerSearch?.visibility=View.VISIBLE
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =layoutInflater.inflate(R.layout.fragment_main,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnTrayAgain:MaterialButton=view.findViewById(R.id.btn_loadAgain)
        val txt_stableLatestProduct:TextView=view.findViewById(R.id.txt_stable_latestProduct)
        val txt_stablePopProduct:TextView=view.findViewById(R.id.txt_stable_popProduct)
        val recyclerViewPop:RecyclerView=view.findViewById(R.id.recycler_main_pop)
        recyclerSearch=view.findViewById(R.id.recycler_search)
        recyclerSearch?.layoutManager=GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
        recyclerViewPop.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        val recyclerViewLatest:RecyclerView=view.findViewById(R.id.recycler_main_latest)
        recyclerViewLatest.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        val tabLayout:TabLayout=view.findViewById(R.id.tab_layout_main)
        val viewPager:ViewPager2=view.findViewById(R.id.viewPagerMain)
        val searchEt:EditText=view.findViewById(R.id.et_search)
        val scrollMain:NestedScrollView=view.findViewById(R.id.scrollMain)
        searchEt.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.i("SearchEt", "beforeTextChanged: "+s.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty())
                {
                    scrollMain.visibility=View.VISIBLE
                }else
                    scrollMain.visibility=View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                Filter(s.toString(),recyclerAdapterLatest.productLatestList)
            }

        })
        val btnViewMoreLatest:MaterialButton=view.findViewById(R.id.btnViewMoreLatest)
        btnViewMoreLatest.setOnClickListener({
            val intent=Intent(requireContext(),ProductListActivity::class.java)
            intent.putExtra("data", sortByLatestProduct)
            startActivity(intent)
        })
        val btnViewMorePop:MaterialButton=view.findViewById(R.id.btnViewMorePop)
        btnViewMorePop.setOnClickListener({
            val intent=Intent(requireContext(),ProductListActivity::class.java)
            intent.putExtra("data", sortByPopularProduct)
            startActivity(intent)
        })
        recyclerAdapterLatest.productclick=this
        recyclerAdapterPop.productClick=this
         mainViewModel.productLiveData.observe(viewLifecycleOwner,{
            recyclerAdapterLatest.productLatestList=it as ArrayList<productData>
             oldProducts.clear()
             oldProducts.addAll(recyclerAdapterLatest.productLatestList)
             recyclerViewLatest.adapter=recyclerAdapterLatest
         })
        mainViewModel.progressLiveData.observe(viewLifecycleOwner,{
            btnTrayAgain.visibility=View.GONE
            setProgress(it)
            if (!errorConnect.isNullOrEmpty())
            {
                txt_stableLatestProduct.visibility=View.GONE
                txt_stablePopProduct.visibility=View.GONE
                btnViewMoreLatest.visibility=View.GONE
                btnViewMorePop.visibility=View.GONE
                btnTrayAgain.visibility=View.VISIBLE
                showSnackBar(errorConnect)
            }
            else
            {
                txt_stableLatestProduct.visibility=View.VISIBLE
                txt_stablePopProduct.visibility=View.VISIBLE
                btnViewMoreLatest.visibility=View.VISIBLE
                btnViewMorePop.visibility=View.VISIBLE
            }
        })
        btnTrayAgain.setOnClickListener({
            errorConnect=""
            mainViewModel.LoadAllItemProduct()

        })
        mainViewModel.productPopLiveData.observe(viewLifecycleOwner,{
            recyclerAdapterPop.productPopList=it as ArrayList<productData>
            recyclerViewPop.adapter=recyclerAdapterPop
            it.forEach {product->
                when(product.id)
                {
                    4->productBanner1=product
                    5->productBanner2=product
                    2->productBanner3=product
                }
            }
        })
        mainViewModel.bannerLiveData.observe(viewLifecycleOwner){
            viewPager.adapter=viewPagerAdapter(this,it)
          val width=  viewPager.width-convertDpToPixel(32f,requireContext()).toInt()
            val height=(width*173)/328
            viewPager.layoutParams.height=height
            TabLayoutMediator(tabLayout,viewPager){
                page,tab->
            }.attach()
        }
        BannerNumberLiveData.observe(viewLifecycleOwner){
            if (it>0)
            {
                Log.i("requier", "onViewCreated: "+requireContext())
                val intent=Intent(requireContext(),ProductDetailsActivity::class.java)
                when(it){
                    1001->intent.putExtra("data",productBanner1)
                    1002->intent.putExtra("data",productBanner2)
                    1003->intent.putExtra("data",productBanner3)
                    else->intent.putExtra("data",productBanner1)
                }
                startActivity(intent)
                BannerNumberLiveData.value=0
            }

        }
    }

    override fun onClickProduct(productData: productData) {
        val intent=Intent(requireContext(),ProductDetailsActivity::class.java)
        intent.putExtra("data",productData)
        startActivity(intent)
    }

    override fun onClickFavoriteProduct(productData: productData) {
        mainViewModel.addOrRemoveFavorite(productData)
       recyclerAdapterPop.changeItem(productData)
    }

    override fun productPopItemClick(productData: productData) {
        val intent=Intent(requireContext(),ProductDetailsActivity::class.java)
        intent.putExtra("data",productData)
        startActivity(intent)
    }

    override fun productPopFavorite(productData: productData) {
        mainViewModel.addOrRemoveFavorite(productData)
        recyclerAdapterLatest.changeItem(productData)
    }

    override fun onResume() {
        super.onResume()
        if (bannerCountLoad>0)
        {
            bannerCountLoad++
            mainViewModel.getBanner()
        }
        else
        {
            bannerCountLoad++
        }
        if (deleteOrAddItemFavorite)
        {
            mainViewModel.LoadAllItemProduct()
            deleteOrAddItemFavorite=false
        }
    }
}