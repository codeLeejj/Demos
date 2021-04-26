package com.annis.mydemos.ui.system

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.annis.mydemos.R
import com.annis.mydemos.braodcastReceiver.NetworkReceiver

class NetWorkStateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net_work_state)

        register()
    }

    lateinit var networkReceiver: NetworkReceiver
    private fun register() {
        networkReceiver = NetworkReceiver();

        val filter = IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    private fun unregister() {
        unregisterReceiver(networkReceiver)
    }
}