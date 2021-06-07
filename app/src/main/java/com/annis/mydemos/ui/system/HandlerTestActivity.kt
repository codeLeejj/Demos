package com.annis.mydemos.ui.system

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.annis.mydemos.R

class HandlerTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handler_test)
        findViewById<Button>(R.id.btSend).setOnClickListener {
            test()
        }
        findViewById<Button>(R.id.btStartThread).setOnClickListener {
            startAThread()
        }
        findViewById<Button>(R.id.btHandlerThread).setOnClickListener {
            handlerThread()
        }
    }

    private fun test() {
        val handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == 1) {
                    Toast.makeText(this@HandlerTestActivity, "what==1", Toast.LENGTH_SHORT).show()
                }
            }
        }

        Log.w("HandlerTestActivity", "${handler.toString()}")
        Log.w("HandlerTestActivity", "${handler.looper.toString()}")
        Log.w("HandlerTestActivity", "${handler.looper.thread.id}")

        val handler2 = object : Handler() {}
        Log.w("HandlerTestActivity", "${handler2.toString()}")
        Log.w("HandlerTestActivity", "${handler2.looper.toString()}")
        Log.w("HandlerTestActivity", "${handler2.looper.thread.id}")
        val msg = Message()
        msg.what = 1
        handler.sendMessageDelayed(msg, 1_000)
//        msg.target = null
        handler.looper
        handler.postDelayed({

        }, 1_000)
    }

    private fun handlerThread() {
        val handlerThread = HandlerThread("Handler Test")
        handlerThread.start()
        handlerThread.looper
    }

    private fun startAThread() {
        Thread {
            run {
                mainLooper
                Looper.myLooper()
                Log.w("HandlerTestActivity a", "${Thread.currentThread().id}")
                Looper.prepare()
                Log.w("HandlerTestActivity a", "${Thread.currentThread().id}")
                Looper.loop()
            }
        }.start()

    }
}