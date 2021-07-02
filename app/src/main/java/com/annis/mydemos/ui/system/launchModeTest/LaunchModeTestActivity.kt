package com.annis.mydemos.ui.system.launchModeTest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.annis.mydemos.R

class LaunchModeTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_mode_test)
        title = "LaunchModeTestActivity"

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
}