package com.example.nikeprojectfinaltest2.feucher.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.common.NikeCompletable
import com.example.nikeprojectfinaltest2.common.baseFragment
import com.google.android.material.button.MaterialButton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentLogin:baseFragment() {
    val view_modelUser:viewModelUser by viewModel()
    val compositeDisposable=CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth_login,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnLogin:MaterialButton=view.findViewById(R.id.btn_login)
        val etLogin:EditText=view.findViewById(R.id.et_login)
        val btnLoginToSingUp:MaterialButton=view.findViewById(R.id.btn_loginToSignUp)
        val etPassword:EditText=view.findViewById(R.id.et_password)
        btnLogin.setOnClickListener({
            view_modelUser.login(etLogin.text.toString(),etPassword.text.toString()).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(object :NikeCompletable(compositeDisposable){
                    override fun onComplete() {
                        requireActivity().finish()
                    }
                })
        })
        btnLoginToSingUp.setOnClickListener({
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frame_auth,FragmentSingUp()).commit()
        })
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}