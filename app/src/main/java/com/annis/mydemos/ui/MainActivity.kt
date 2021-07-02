package com.annis.mydemos.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.annis.mydemos.R
import com.annis.mydemos.databinding.ActivityMainBinding
import com.annis.mydemos.ui.main.systemTest.HomeFragment
import com.annis.mydemos.utils.ActivityStack

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        Log.w("MainActivity", "${System.currentTimeMillis()}")
        Log.w("MainActivity", "${SystemClock.uptimeMillis()}")


        ActivityStack.getInstance().add(this)
    }

    override fun onDestroy() {
        ActivityStack.getInstance().remove(this)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == HomeFragment.INTENT_CAMERA_REQUEST) {
                val uri = data?.data
            }
        }
    }

    private fun test() {
        val handler: Handler? = null
//        val handler: LooperThread? = null
    }
}