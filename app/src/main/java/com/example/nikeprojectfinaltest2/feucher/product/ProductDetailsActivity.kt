package com.example.nikeprojectfinaltest2.feucher.product

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.common.AuthStart
import com.example.nikeprojectfinaltest2.common.NikeCompletable
import com.example.nikeprojectfinaltest2.common.baseActivity
import com.example.nikeprojectfinaltest2.data.Comment
import com.example.nikeprojectfinaltest2.data.TokenContainer
import com.example.nikeprojectfinaltest2.data.productData
import com.example.nikeprojectfinaltest2.databinding.ActivityProductDetailsBinding
import com.example.nikeprojectfinaltest2.feucher.auth.AuthActivity
import com.example.nikeprojectfinaltest2.feucher.cart.BannerCartCount
import com.example.nikeprojectfinaltest2.feucher.comment.AddCommentActivity
import com.example.nikeprojectfinaltest2.feucher.comment.CommentActivity
import com.example.nikeprojectfinaltest2.feucher.main.mainViewModel
import com.example.nikeprojectfinaltest2.repo.favoriteIdsLiveData
import com.example.nikeprojectfinaltest2.repo.favoriteListIds
import com.example.nikeprojectfinaltest2.service.ImageLoadingService
import com.example.nikeprojectfinaltest2.utils.farsi
import com.example.nikeprojectfinaltest2.view.NikeImageView
import com.example.nikeprojectfinaltest2.view.scroll.ObservableScrollView
import com.example.nikeprojectfinaltest2.view.scroll.ObservableScrollViewCallbacks
import com.example.nikeprojectfinaltest2.view.scroll.ScrollState
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
var bannerCountLoad=1
var deleteOrAddItemFavorite=false
var onClickBtnFavoriteOfProductList=false
var commentAdd=2
var getProduct:productData?=null
var messageComment=""
class ProductDetailsActivity : baseActivity() {
    lateinit var binding:ActivityProductDetailsBinding
    val productViewModel:ProductViewModel by viewModel { parametersOf(intent.extras) }
    val imageLoadingService:ImageLoadingService by inject()
    val compositeDisposable=CompositeDisposable()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val observableScroll:ObservableScrollView=findViewById(R.id.observable_scroll_product)
        val cardViewProductDetails:MaterialCardView=findViewById(R.id.cardProductDetails)
        val txtTitleProductDetails:TextView=findViewById(R.id.txt_title_productDetails)
        val txtTitleProductToolbar:TextView=findViewById(R.id.txt_toolbarTitle_productDetails)
        val txtNewPriceProductDetails:TextView=findViewById(R.id.txt_newPrice_ProductDetail)
        val txtOldPriceDetails:TextView=findViewById(R.id.txt_oldPrice_productDetails)
        val imgProductDetails:NikeImageView=findViewById(R.id.img_productDetails)
        val btn_viewAll:MaterialButton=findViewById(R.id.btn_viewAll)
        val btnAddComment:MaterialButton=findViewById(R.id.btn_addComment)
        val btn_favorite:ImageView=findViewById(R.id.img_favorite)
        val recyclerCommentAdapter:RecyclerComment by inject()
        val recyclerViewComment:RecyclerView=findViewById(R.id.recycler_comment)
        btnAddComment.setOnClickListener({
            if (TokenContainer.accessToken!=null)
            {
            val intent=Intent(this,AddCommentActivity::class.java)
            intent.putExtra("idComment",productViewModel.productLiveDataDetails.value!!.id)
                commentAdd=2
            startActivity(intent)
            }else
            {
                val intent= Intent(this, AuthActivity::class.java)
                AuthStart=1
                startActivity(intent)
            }
        })
        recyclerViewComment.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        productViewModel.productLiveDataDetails.observe(this,{
            favoriteIdsLiveData.observe(this){favoriteList->
                if (favoriteList.contains(it.id))
                {
                    it.isFavorite=true
                }else
                {
                    it.isFavorite=false
                }
            }

            btn_favorite.setImageResource(if (it.isFavorite)R.drawable.baseline_favorite_fill else R.drawable.ic_favorites)
            btn_favorite.setOnClickListener({viewBtn->
                productViewModel.addOrRemoveFavorite(it)
                deleteOrAddItemFavorite=true
                if ( onClickBtnFavoriteOfProductList==true)
                {
                    getProduct=it
                }
                it.isFavorite=!it.isFavorite
                btn_favorite.setImageResource(if (it.isFavorite)R.drawable.baseline_favorite_fill else R.drawable.ic_favorites)
            })
            productViewModel.cartItemCount.observe(this){
                binding.btnAddToCart.text=getString(R.string.addToCart)+" "+"("+it.toString()+")"
            }
            txtTitleProductDetails.setText(it.title)
            txtTitleProductToolbar.setText(it.title)
            txtOldPriceDetails.paintFlags=Paint.STRIKE_THRU_TEXT_FLAG
            txtOldPriceDetails.setText("${farsi(it.previous_price.toString())}"+" "+"تومان")
            txtNewPriceProductDetails.setText("${farsi(it.price.toString())}"+" "+"تومان")
            imageLoadingService.load(imgProductDetails,it.image)
            observableScroll.addScrollViewCallbacks(object :ObservableScrollViewCallbacks{
                override fun onScrollChanged(
                    scrollY: Int,
                    firstScroll: Boolean,
                    dragging: Boolean
                ) {
                    cardViewProductDetails.alpha=scrollY.toFloat()/imgProductDetails.measuredHeight
                    imgProductDetails.translationY=scrollY.toFloat()/2
                }

                override fun onDownMotionEvent() {
                }

                override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {
                }

            })
        })
        productViewModel.commentLiveData.observe(this,{
            if (it.size>3)
            {
                btn_viewAll.visibility= View.VISIBLE
                btn_viewAll.setOnClickListener({views->
                    val intent=Intent(this,CommentActivity::class.java)
                    intent.putExtra("idComment",productViewModel.productLiveDataDetails.value!!.id)
                    startActivity(intent)
                })
            }
            recyclerCommentAdapter.commentList=it as ArrayList<Comment>
            recyclerCommentAdapter.changeComment(it)
            recyclerViewComment.adapter=recyclerCommentAdapter
        })
        binding.btnBackProduct.setOnClickListener({
            if (BannerCartCount==0)
            {
                bannerCountLoad=0
                finish()
            }
            else
            {
                bannerCountLoad= BannerCartCount
                BannerCartCount=0
                finish()
            }

        })
        productViewModel.progressLiveData.observe(this,{
            setProgress(it)
        })

        binding.btnAddToCart.setOnClickListener({
            productViewModel.onClickBtnAddToCart().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :NikeCompletable(compositeDisposable){
                    override fun onComplete() {
                        showSnackBar(getString(R.string.addToCartIsComplete))
                    }

                })

        })
        productViewModel.cartItemCount.observe(this@ProductDetailsActivity){
            binding.btnAddToCart.text=getString(R.string.addToCart)+" "+"("+it.toString()+")"
        }
    }

    override fun onStart() {
        super.onStart()
        AuthStart=0
        favoriteIdsLiveData.postValue(favoriteListIds)    }

    override fun onResume() {
        super.onResume()
        productViewModel.getComment()
    }
}