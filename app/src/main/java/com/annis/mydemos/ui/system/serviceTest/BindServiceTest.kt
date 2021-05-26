package com.annis.mydemos.ui.system.serviceTest

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlin.random.Random

class BindServiceTest : Service() {

    var mBinder = MyBinder()
    override fun onBind(intent: Intent): IBinder? {
        Log.w("ServiceTest", "Bind Service onBind")
        return mBinder
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Log.w("ServiceTest", "Bind Service onRebind")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.w("ServiceTest", "Bind Service onUnbind")
        return super.onUnbind(intent)
    }

    inner class MyBinder : Binder() {
        fun getService(): BindServiceTest {
            return this@BindServiceTest
        }
    }

    private var myTag: String? = null
    override fun onCreate() {
        super.onCreate()
        Log.w("ServiceTest", "Bind Service onCreate")
        myTag = Random(21).toString()
    }

    override fun onDestroy() {
        Log.w("ServiceTest", "Bind Service onDestroy")
        super.onDestroy()
    }
    val tag: String?
        get() = myTag
}