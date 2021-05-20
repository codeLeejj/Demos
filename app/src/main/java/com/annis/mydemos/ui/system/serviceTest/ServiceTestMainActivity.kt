package com.annis.mydemos.ui.system.serviceTest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.annis.mydemos.R

class ServiceTestMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_test_main)

        findViewById<Button>(R.id.tvStartService).setOnClickListener {

            Log.w("ServiceTest", "Thread Id:${Thread.currentThread().id}")
            startService(Intent(this, StartServiceTest::class.java))
        }
        findViewById<Button>(R.id.tvBindService).setOnClickListener {

        }
    }
}