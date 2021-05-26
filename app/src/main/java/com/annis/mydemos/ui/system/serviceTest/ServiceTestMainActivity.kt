package com.annis.mydemos.ui.system.serviceTest

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import com.annis.mydemos.R

class ServiceTestMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_test_main)

        findViewById<Button>(R.id.tvStartService).setOnClickListener {
            Log.w("ServiceTest", "Start Service Thread Id:${Thread.currentThread().id}")
            startService(Intent(this, StartServiceTest::class.java))
        }
        findViewById<Button>(R.id.tvStopService).setOnClickListener {
//            stopService(Intent(this, StartServiceTest::class.java))
            stopService(Intent(this, StartServiceTest::class.java))
        }

        findViewById<Button>(R.id.tvBindService).setOnClickListener {
            Log.w("ServiceTest", "Bind Service click")
            bindService(Intent(this, BindServiceTest::class.java), mConn, Service.BIND_ABOVE_CLIENT)
        }

        findViewById<Button>(R.id.tvUnbindService).setOnClickListener {
            service1?.let {
                unbindService(mConn)
            }
        }
    }

    var service1: BindServiceTest? = null
    var mConn = object : ServiceConnection {
        var binder: BindServiceTest.MyBinder? = null
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.w("ServiceTest", "Bind Service onServiceConnected ")

            binder = (service as BindServiceTest.MyBinder)
            service1 = binder?.getService()
            Log.w("ServiceTest", "获取 tag:${binder?.getService()?.tag}")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.w("ServiceTest", "Bind Service onServiceDisconnected")
        }
    }
}