package com.example.nikeprojectfinaltest2.feucher.checkout

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.common.baseActivity
import com.example.nikeprojectfinaltest2.feucher.main.MainActivity
import com.example.nikeprojectfinaltest2.feucher.order.OrderHistory
import com.example.nikeprojectfinaltest2.utils.farsi
import com.google.android.material.button.MaterialButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
var checkoutBtnClick= false
class CheckoutActivity : baseActivity() {
    var uri:Uri?=null
    val viewModel:ViewModelCheckout by viewModel {
        uri=intent.data
        if (uri!=null)
        {
            parametersOf(uri?.getQueryParameter("order_id")!!.toInt())
        }
        else
        {
            parametersOf(intent.extras!!.getInt("Id"))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        val txt_paymentSuccess:TextView=findViewById(R.id.txt_paymentSuccess)
        val img_emptyCheckout:ImageView=findViewById(R.id.img_emptyCheckOut)
        val txt_paymentStatus:TextView=findViewById(R.id.txt_paymentStatus)
        val txt_paymentPrice:TextView=findViewById(R.id.txt_paymentPrice)
        val txt_tanks:TextView=findViewById(R.id.txt_tanks)
        val btnBackToHome:MaterialButton=findViewById(R.id.btn_backToHome)
        btnBackToHome.setOnClickListener({
            val intent= Intent(this,MainActivity::class.java)
            checkoutBtnClick=true
            startActivity(intent)
        })
        val btnPayHistory:MaterialButton=findViewById(R.id.btn_paymentHistory)
        btnPayHistory.setOnClickListener({
            val intent= Intent(this,OrderHistory::class.java)
            checkoutBtnClick=true
            startActivity(intent)
        })
        viewModel.checkoutLiveData.observe(this){
            txt_paymentSuccess.setTextColor(Color.parseColor("#217CF3"))
            if (uri!=null)
            {
                if (it.purchase_success)
                {
                    txt_paymentSuccess.text="پرداخت موفق"
                    txt_tanks.visibility=View.VISIBLE
                    txt_paymentStatus.text=it.payment_status
                    txt_paymentPrice.setText("${farsi(it.payable_price.toString())}"+" "+"تومان")
                    img_emptyCheckout.setImageResource(R.drawable.cart_payment_success)
                }else{
                    img_emptyCheckout.setImageResource(R.drawable.denied_payment)
                    txt_paymentSuccess.setTextColor(Color.parseColor("#CD0000"))
                    txt_paymentSuccess.text="پرداخت ناموفق"
                    txt_tanks.visibility=View.GONE
                    txt_paymentStatus.text=it.payment_status
                    txt_paymentPrice.setText("${farsi(it.payable_price.toString())}"+" "+"تومان")
                }
            }else{

                txt_paymentSuccess.text="سفارش شما ثبت شد"
                txt_tanks.visibility=View.VISIBLE
                txt_paymentStatus.text="در حال پردازش"
                txt_paymentPrice.setText("${farsi(it.payable_price.toString())}"+" "+"تومان")
                img_emptyCheckout.setImageResource(R.drawable.cart_payment_success)
            }
        }
        val btnBack:ImageView=findViewById(R.id.btn_backCheckout)
        btnBack.setOnClickListener({
            finish()
        })
    }

    override fun onResume() {
        super.onResume()
        if (checkoutBtnClick)
        {
            val intent= Intent(this,MainActivity::class.java)
            checkoutBtnClick=true
            startActivity(intent)
        }
    }
}