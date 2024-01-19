package com.example.nikeprojectfinaltest2.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.health.connect.datatypes.units.Length
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.feucher.auth.AuthActivity
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
var AuthStart=0
abstract class  baseFragment:Fragment(),baseView{
    override val rootView: CoordinatorLayout?
        get() =view as CoordinatorLayout
    override val viewContext: Context?
        get() = context

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}
abstract class baseActivity:AppCompatActivity(),baseView{
    override val rootView: CoordinatorLayout?
        @SuppressLint("SuspiciousIndentation")
        get(){
        val ViewGroup  = window.decorView.rootView.findViewById(android.R.id.content) as ViewGroup
            if (ViewGroup is CoordinatorLayout)
            {
                return ViewGroup
            }
            else
            {
                ViewGroup.children.forEach {
                    if (it is CoordinatorLayout)
                        return it
                }
            }
          throw IllegalStateException("RootViewMustBeCoordinator")

        }
    override val viewContext: Context?
        get() = this

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
}
abstract class baseViewModel:ViewModel()
{
    val progressLiveData= MutableLiveData<Boolean>()
    val compositeDisposable= CompositeDisposable()
    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
interface baseView{
    val rootView:CoordinatorLayout?
    val viewContext:Context?
    fun setEmptyState(resId:Int):View?
    {
        rootView?.let {rootsview->
            viewContext.let {context->
                var emptyState=rootsview.findViewById<View>(R.id.empty_StateLayout)
                if (emptyState==null)
                {
                    emptyState=LayoutInflater.from(context).inflate(resId,rootsview,false)
                    rootsview.addView(emptyState)
                }
                emptyState.visibility=View.VISIBLE
                return  emptyState
            }
        }

        return null
    }
    fun setProgress(mustShow:Boolean)
    {
        rootView?.let {
            viewContext?.let {context ->
                var loadingView=it.findViewById<View>(R.id.loading_layout)
                if (loadingView==null&&mustShow==true)
                {
                    loadingView=LayoutInflater.from(context).inflate(R.layout.loading_layout,it,false)
                    it.addView(loadingView)
                }
                loadingView?.visibility=if (mustShow)View.VISIBLE else View.GONE
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showError(nikeException: NikeException)
    {
        viewContext?.let {
            when(nikeException.type)
            {
                NikeException.Type.Simple->showSnackBar(nikeException.serverMessage?:it.getString(nikeException.userFriendlyMessage))
                NikeException.Type.Auth->{
                    if (AuthStart==0)
                    {
                        it.startActivity(Intent(it,AuthActivity::class.java))
                        Toast.makeText(it,nikeException.serverMessage,Toast.LENGTH_LONG).show()
                    }
                    else{
                        showSnackBar(it.getString(R.string.falseInfo))
                    }

                }
            }
        }
    }
    fun showSnackBar(message: String,length: Int=Snackbar.LENGTH_LONG)
    {
        rootView?.let {
           val snackbar= Snackbar.make(it,message,length)
            snackbar.view.setBackgroundColor(Color.parseColor("#217CF3"))
                snackbar.show()
        }
    }
}