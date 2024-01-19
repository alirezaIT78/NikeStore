package com.example.nikeprojectfinaltest2.feucher.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.common.AuthStart
import com.example.nikeprojectfinaltest2.common.baseFragment
import com.example.nikeprojectfinaltest2.data.TokenContainer
import com.example.nikeprojectfinaltest2.feucher.auth.AuthActivity
import com.example.nikeprojectfinaltest2.feucher.checkout.checkoutBtnClick
import com.example.nikeprojectfinaltest2.feucher.favorite.FavoriteActivity
import com.example.nikeprojectfinaltest2.feucher.main.MainActivity
import com.example.nikeprojectfinaltest2.feucher.order.OrderHistory
import com.example.nikeprojectfinaltest2.feucher.product.bannerCountLoad
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentProfile:baseFragment(){
    val viewModel:ProfileViewModel by viewModel()
    var txtLogin:TextView?=null
    var txtUsername:TextView?=null
    var txtFavorite:TextView?=null
    var txtPaymentHistory:TextView?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         val view:View=LayoutInflater.from(context).inflate(R.layout.fragment_profile,container,false)
        txtLogin=view.findViewById(R.id.txt_loginProfile)
        txtUsername=view.findViewById(R.id.txt_usernameProfile)
        txtFavorite=view.findViewById(R.id.txt_favoriteProfile)
        txtPaymentHistory=view.findViewById(R.id.txt_paymentHistoryProfile)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtFavorite?.setOnClickListener({
            val intent=Intent(requireContext(),FavoriteActivity::class.java)
            startActivity(intent)
        })
        txtPaymentHistory?.setOnClickListener({
            if (TokenContainer.accessToken!=null){
                val intent= Intent(requireContext(), OrderHistory::class.java)
                startActivity(intent)
            }else {
                val intent = Intent(requireContext(), AuthActivity::class.java)
                AuthStart=1
                startActivity(intent)
            }
        })
    }
   fun checkAuthState()
   {
       if (viewModel.isSignIn)
       {
           txtUsername?.text=viewModel.username
           txtLogin?.text=getString(R.string.exitAccount)
           txtLogin?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_logout_24,0,0,0)
           txtLogin?.setOnClickListener({
               viewModel.signOut()
               viewModel.cartCount.getCartCount()
               checkAuthState()
           })
       }else{
           txtLogin?.text=getString(R.string.loginToAccount)
           txtUsername?.text="کاربر مهمان"
           txtLogin?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_login_24,0,0,0)
           txtLogin?.setOnClickListener({
               val intent=Intent(requireContext(),AuthActivity::class.java)
               AuthStart=1
               startActivity(intent)
           })
       }
   }

    override fun onResume() {
        super.onResume()
        bannerCountLoad++
        checkAuthState()
    }
}