package com.example.nikeprojectfinaltest2.feucher.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.common.NikeCompletable
import com.example.nikeprojectfinaltest2.common.baseFragment
import com.google.android.material.button.MaterialButton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSingUp:baseFragment() {
    val view_modelUser:viewModelUser by viewModel()
    val compositeDisposable=CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth_sing_up,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val et_email:EditText=view.findViewById(R.id.et_email_Singup)
        val et_password:EditText=view.findViewById(R.id.et_pass_Singup)
        val btn_singUp:MaterialButton=view.findViewById(R.id.btn_singUp)
        val btn_singUpToLogin:MaterialButton=view.findViewById(R.id.btn_sinUpToLogin)
        btn_singUp.setOnClickListener({
            view_modelUser.singUp(et_email.text.toString(),et_password.text.toString()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(object :NikeCompletable(compositeDisposable){
                    override fun onComplete() {
                        Toast.makeText(context," خوش آمدید${et_email.text}",Toast.LENGTH_LONG).show()
                        requireActivity().finish()
                    }
                })
        })
        btn_singUpToLogin.setOnClickListener({
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frame_auth,FragmentLogin()).commit()
        })
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}