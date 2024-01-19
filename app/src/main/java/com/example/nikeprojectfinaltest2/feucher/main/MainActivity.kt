package com.example.nikeprojectfinaltest2.feucher.main

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.nikeprojectfinaltest.data.CartItemCount
import com.example.nikeprojectfinaltest2.R
import com.example.nikeprojectfinaltest2.common.baseActivity
import com.example.nikeprojectfinaltest2.common.setupWithNavController
import com.example.nikeprojectfinaltest2.utils.convertDpToPixel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : baseActivity() {
    private var currentNavController: LiveData<NavController>? = null
   val viewModel:ViewModelActivityMain by viewModel()
    var navigationView:BottomNavigationView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
            navigationView=findViewById(R.id.bottomNavigationMain)
        } // Else, need to wait for onRestoreInstanceState
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationMain)

        val navGraphIds = listOf(R.navigation.nav_home, R.navigation.nav_cart, R.navigation.nav_profile)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeCartCount(cartItemCount: CartItemCount)
    {
        val badge=navigationView?.getOrCreateBadge(R.id.nav_cart)
        badge?.let {
            it.backgroundColor=Color.parseColor("#217CF3")
            it.badgeGravity=BadgeDrawable.BOTTOM_START
            it.verticalOffset= convertDpToPixel(10f,this).toInt()
            it.number=cartItemCount.count
            it.isVisible=cartItemCount.count>0
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCartCount()
    }
}