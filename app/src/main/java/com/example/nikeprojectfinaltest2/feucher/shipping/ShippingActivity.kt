package com.example.nikeprojectfinaltest2.feucher.shipping

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import com.example.nikeprojectfinaltest.data.Purchase
import com.example.nikeprojectfinaltest.data.SubmitOrderRequest
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.common.myObserver
import com.example.nikeprojectfinaltest2.feucher.cart.RecyclerCartAdapter
import com.example.nikeprojectfinaltest2.feucher.cart.purchaseGlobal
import com.example.nikeprojectfinaltest2.feucher.checkout.CheckoutActivity
import com.example.nikeprojectfinaltest2.utils.openUrlInCustomTab
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel
class ShippingActivity : AppCompatActivity() {
    val viewModel:ShippingViewModel by viewModel()
    val compositeDisposable=CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipping)
        val viewPurchase:View=findViewById(R.id.viewPurchaseShipping)
        val btnBack:ImageView=findViewById(R.id.img_backShipping)
        val etName:TextInputEditText=findViewById(R.id.mEt_name)
        val etIName:TextInputLayout=findViewById(R.id.mIl_name)
        val etFName:TextInputEditText=findViewById(R.id.mEt_Fname)
        val etIFName:TextInputLayout=findViewById(R.id.mIl_Fname)
        val etPostalCode:TextInputEditText=findViewById(R.id.mEt_postalCode)
        val etIPostalCode:TextInputLayout=findViewById(R.id.mIl_postalCode)
        val etPhoneNumber:TextInputEditText=findViewById(R.id.mEt_phoneNumber)
        val eILPhoneNumber:TextInputLayout=findViewById(R.id.mIl_phoneNumber)
        val etAddress:TextInputEditText=findViewById(R.id.mEt_address)
        val eILAddress:TextInputLayout=findViewById(R.id.mIl_address)
        val btn_Submit:MaterialButton=findViewById(R.id.btnPayOnline)
        val btnDelivery:MaterialButton=findViewById(R.id.btnPayDelivery)
        val onClickListener=OnClickListener{
            if (etName.length()>=3&&etFName.length()>=3&&etPhoneNumber.length()==11&&etPostalCode.length()==10&&etAddress.length()<50)
            {
                viewModel.submitOrder(etName.text.toString(),etFName.text.toString(),etPostalCode.text.toString()
                ,etPhoneNumber.text.toString(),etAddress.text.toString(),
                    if (it==btn_Submit) PAYMENT_ONLINE else PAYMENT_DELIVERY)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object :myObserver<SubmitOrderRequest>(compositeDisposable){
                        @SuppressLint("SuspiciousIndentation")
                        override fun onSuccess(t: SubmitOrderRequest) {
                           if (t.bank_gateway_url.isNotEmpty())
                                openUrlInCustomTab(this@ShippingActivity,t.bank_gateway_url)
                           else{
                             val intent= Intent(this@ShippingActivity,CheckoutActivity::class.java)
                               intent.putExtra("Id",t.order_id)
                               startActivity(intent)
                           }
                            finish()
                        }
                    })
            }else{

                if (etName.length()<3)
                    etIName.setError("فیلد نام نباید کمتر از ۳ باشد")
                if (etFName.length()<3)
                    etIFName.setError("فیلد نام خانوادگی نباید کمتر از ۳ باشد")
                if (etPostalCode.length()!=10)
                    etIPostalCode.setError("فیلد کدپستی باید شامل ۱۰ عدد باشد")
                if (etPhoneNumber.length()!=11)
                    eILPhoneNumber.setError("فیلد تلفن همراه باید شامل ۱۱ عدد باشد")
                if (etAddress.length()>50)
                    eILAddress.setError("فیلد آدرس نباید بیشتر از ۵۰ باشد")
            }
        }
        btn_Submit.setOnClickListener(onClickListener)
        btnDelivery.setOnClickListener(onClickListener)
        btnBack.setOnClickListener({
            finish()
        })
        val viewHolderPurchase=RecyclerCartAdapter.purchaseVh(viewPurchase)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val purchase= intent.getParcelableExtra("data",Purchase::class.java)
            purchase?.let {
                viewHolderPurchase.bindPurchase(it.total_price,it.shipping_cost,it.payable_price)
            }
        } else {
            purchaseGlobal?.let {
                viewHolderPurchase.bindPurchase(it.total_price,it.shipping_cost,it.payable_price)
            }
        }

    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}