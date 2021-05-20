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
        Log.w("ServiceTest", "Thread Id:${Thread.currentThread().id}")
    }
}