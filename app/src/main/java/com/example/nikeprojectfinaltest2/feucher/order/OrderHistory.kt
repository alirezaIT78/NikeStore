package com.example.nikeprojectfinaltest2.feucher.order
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.common.baseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderHistory : baseActivity() {
    val viewModel:OrderHistoryViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)
        val recyclerView:RecyclerView=findViewById(R.id.recyclerOrderHistory)
        recyclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        val btnBack:ImageView=findViewById(R.id.btnBackOrderHistory)
        btnBack.setOnClickListener({
            finish()
        })
        viewModel.progressLiveData.observe(this){
            setProgress(it)
        }
        viewModel.orderHistoryItemLiveData.observe(this){
            val recyclerAdapterOrderHistory=RecyclerAdapterOrderHistory(this,it)
            recyclerView.adapter=recyclerAdapterOrderHistory
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getOrders()
    }
}