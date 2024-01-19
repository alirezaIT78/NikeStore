package com.example.nikeprojectfinaltest2.feucher.comment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.common.AuthStart
import com.example.nikeprojectfinaltest2.common.baseActivity
import com.example.nikeprojectfinaltest2.data.Comment
import com.example.nikeprojectfinaltest2.data.TokenContainer
import com.example.nikeprojectfinaltest2.databinding.ActivityCommentBinding
import com.example.nikeprojectfinaltest2.feucher.auth.AuthActivity
import com.example.nikeprojectfinaltest2.feucher.product.RecyclerComment
import com.example.nikeprojectfinaltest2.feucher.product.commentAdd
import com.example.nikeprojectfinaltest2.feucher.product.messageComment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CommentActivity : baseActivity() {
    lateinit var binding:ActivityCommentBinding
    val commentViewModel:viewModelComment by viewModel { parametersOf(intent.extras!!.getInt("idComment")) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerViewAllComment:RecyclerView=findViewById(R.id.recycler_allComment)
        recyclerViewAllComment.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        val recyclerAdapterAllComment:RecyclerComment by inject()
        commentViewModel.commentAllViewModel.observe(this,{
            binding.floatAddComment.visibility=View.VISIBLE
            recyclerAdapterAllComment.commentList=it as ArrayList<Comment>
            recyclerAdapterAllComment.booleanViewAll=true
            recyclerAdapterAllComment.changeComment(it)
            recyclerViewAllComment.adapter=recyclerAdapterAllComment
        })
        binding.btnBackComment.setOnClickListener({
            finish()
        })
        commentViewModel.progressLiveData.observe(this,{
            setProgress(it)
        })
        binding.floatAddComment.setOnClickListener({
            if (TokenContainer.accessToken!=null)
            {
              val intent= Intent(this,AddCommentActivity::class.java)
              intent.putExtra("idComment",commentViewModel.productId)
                commentAdd=2
                startActivity(intent)
            }else
            {
                val intent= Intent(this,AuthActivity::class.java)
                AuthStart=1
                commentAdd=2
                startActivity(intent)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.floatAddComment.visibility= View.GONE
        commentViewModel.getComment()
        if (!messageComment.isEmpty())
        {
            showSnackBar(messageComment)
            messageComment =""
        }
    }
}