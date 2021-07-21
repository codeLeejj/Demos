package com.annis.mydemos.ui.system.launchModeTest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.annis.mydemos.R

class SingleTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_mode)
        title = "SingleTaskActivity"
        Log.i("launchModeLog", "$this  onCreate")
        findViewById<Button>(R.id.btStandard).setOnClickListener {
            startActivity(Intent(this, StandardActivity::class.java))
        }
        findViewById<Button>(R.id.btSingleTask).setOnClickListener {
            startActivity(Intent(this, SingleTaskActivity::class.java))
        }
        findViewById<Button>(R.id.btSingleTop).setOnClickListener {
            startActivity(Intent(this, SingleTopActivity::class.java))
        }
        findViewById<Button>(R.id.btSingleInstance).setOnClickListener {
            startActivity(Intent(this, SingleInstanceActivity::class.java))
        }
    }

    val NAME = "SingleTaskActivity"
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i("launchModeLog", "$this  onNewIntent")
    }

    override fun onStart() {
        super.onStart()
        Log.i("launchModeLog", "$this  onStart")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("launchModeLog", "$this  onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("launchModeLog", "$this  onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("launchModeLog", "$this  onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("launchModeLog", "$this  onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("launchModeLog", "$this  onDestroy")
    }
}