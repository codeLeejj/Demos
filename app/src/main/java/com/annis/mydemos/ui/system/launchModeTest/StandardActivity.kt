package com.annis.mydemos.ui.system.launchModeTest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.annis.mydemos.R

class StandardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_mode)

        findViewById<Button>(R.id.btStandard).setOnClickListener {
            startActivity(Intent(this,StandardActivity::class.java))
        }
        findViewById<Button>(R.id.btSingleTask).setOnClickListener {
            startActivity(Intent(this,SingleTaskActivity::class.java))
        }
        findViewById<Button>(R.id.btSingleTop).setOnClickListener {
            startActivity(Intent(this,SingleTopActivity::class.java))
        }
        findViewById<Button>(R.id.btSingleInstance).setOnClickListener {
            startActivity(Intent(this,SingleInstanceActivity::class.java))
        }

        Log.w("StandardActivity","onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.w("StandardActivity","onStart")
    }

    override fun onRestart() {
        super.onRestart()
        Log.w("StandardActivity","onRestart")
    }
    override fun onResume() {
        super.onResume()
        Log.w("StandardActivity","onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.w("StandardActivity","onPause")
    }
    override fun onStop() {
        super.onStop()
        Log.w("StandardActivity","onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("StandardActivity","onDestroy")
    }
}