package com.annis.mydemos.ui.system.serviceTest

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class StartServiceTest : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.w("ServiceTest", "Start Service onCreate,Thread Id:${Thread.currentThread().id}")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.w("ServiceTest", "Start Service onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onDestroy() {
        super.onDestroy()

        Log.w("ServiceTest", "Start Service onDestroy")
    }
}