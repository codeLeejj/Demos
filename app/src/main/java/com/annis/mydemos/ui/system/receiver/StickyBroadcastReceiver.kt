package com.annis.mydemos.ui.system.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

import android.content.pm.PackageManager


class StickyBroadcastReceiver : BroadcastReceiver() {
    companion object {
        val Action = "com.sample.test.sticky.broadcast.receiver"
    }

    val PERMISSION = "com.sample.test.permission.sticky.receiver"

    override fun onReceive(context: Context, intent: Intent) {
        val checkCallingOrSelfPermission = context.checkCallingOrSelfPermission(PERMISSION)
        if (PackageManager.PERMISSION_GRANTED == checkCallingOrSelfPermission) {//权限判断
            Toast.makeText(context, "授权成功", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT).show()
            throw RuntimeException("permission denied")
        }
        if (intent != null && Action == intent.action) {
            Toast.makeText(context, intent.getStringExtra("info"), Toast.LENGTH_SHORT).show()
        }
    }
}