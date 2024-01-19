package com.example.nikeprojectfinaltest2.feucher.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nikeprojectfinaltest2.R

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_auth,FragmentLogin())
            commit()
        }
    }
}