package com.annis.mydemos.ui.system.serviceTest

import android.app.Service
import android.content.Intent
import android.os.IBinder

class BindServiceTest : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}