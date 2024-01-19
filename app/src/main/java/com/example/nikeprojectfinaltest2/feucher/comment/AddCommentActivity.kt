package com.example.nikeprojectfinaltest2.feucher.comment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.common.NikeCompletable
import com.example.nikeprojectfinaltest2.common.baseActivity
import com.example.nikeprojectfinaltest2.feucher.product.commentAdd
import com.example.nikeprojectfinaltest2.feucher.product.messageComment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AddCommentActivity : baseActivity() {
    val viewModel:viewModelComment by viewModel{ parametersOf(intent.extras!!.getInt("idComment")) }
    var btnAddComment:MaterialButton?=null
    val compositeDisposable=CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        val productId=intent.extras!!.getInt("idComment")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comment)
        val etTitle:TextInputEditText=findViewById(R.id.tIE_titleCommentAdd)
        val etContent:TextInputEditText=findViewById(R.id.tIE_contentCommentAdd)
        btnAddComment=findViewById(R.id.btn_addCommentToList)
        val btnBack:ImageView=findViewById(R.id.btnBackCommentAdd)
        val progressBar:ProgressBar=findViewById(R.id.progressAddComment)
        btnAddComment?.setOnClickListener({
            btnAddComment?.text=""
            progressBar.visibility=View.VISIBLE
            viewModel.addComment(etTitle.text.toString(),etContent.text.toString()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(object :NikeCompletable(compositeDisposable){
                    override fun onComplete() {
                        progressBar.visibility=View.GONE
                        commentAdd=1
                        messageComment="دیدگاه شما ثبت شد"
                        val intent= Intent(it.context,CommentActivity::class.java)
                        intent.putExtra("idComment",productId)
                        startActivity(intent)
                    }
                })
        })
        btnBack.setOnClickListener({
            finish()
        })

    }

    override fun onResume() {
        super.onResume()
        btnAddComment?.text=getString(R.string.add_comment)
        if (commentAdd ==1)
        {
            finish()
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}