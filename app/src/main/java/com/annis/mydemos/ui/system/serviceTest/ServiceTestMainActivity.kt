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
            Log.w("ServiceTest", "Thread Id:${Thread.currentThread().id}")
            startService(Intent(this, StartServiceTest::class.java))
        }

        findViewById<Button>(R.id.tvBindService).setOnClickListener {
            mConn = MyConn()
            bindService(Intent(this, BindServiceTest::class.java), mConn, Service.BIND_AUTO_CREATE)
        }
    }

    lateinit var mConn: MyConn

    class MyConn : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            //这是意外中断的时候才会调用的方法

        }

    }
}