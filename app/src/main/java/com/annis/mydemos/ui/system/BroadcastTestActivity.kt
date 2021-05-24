package com.annis.mydemos.ui.system

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.annis.mydemos.R
import com.annis.mydemos.ui.system.receiver.StickyBroadcastReceiver
import android.widget.Toast

import android.content.IntentFilter


class BroadcastTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_broastcast_test)

        findViewById<Button>(R.id.btSendStickyBroadcast).setOnClickListener {
            sendStickyBroadcast();
        }
        findViewById<Button>(R.id.btReceiveStickyBroadcast).setOnClickListener {
            receiveStickyBroadcast();
        }
        findViewById<Button>(R.id.btRemoveStickyBroadcast).setOnClickListener {
            stickyIntent?.let {
                removeStickyBroadcast(it)
            }
        }
    }

    var stickyIntent: Intent? = null
    private fun sendStickyBroadcast() {
        stickyIntent = Intent().apply {
            action = StickyBroadcastReceiver.Action
            putExtra("info", "sticky broadcast has been receiver")
        }

        stickyIntent?.let {
            sendStickyBroadcast(it)
        }
    }

    private fun receiveStickyBroadcast() {
        val intentFilter = IntentFilter(StickyBroadcastReceiver.Action)
        val data = registerReceiver(null, intentFilter)
        if (data != null && StickyBroadcastReceiver.Action == data.action) {
            Toast.makeText(this, data.getStringExtra("info"), Toast.LENGTH_SHORT).show()
        }
    }
}