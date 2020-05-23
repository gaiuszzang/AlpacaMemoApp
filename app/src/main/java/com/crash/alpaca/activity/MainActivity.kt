package com.crash.alpaca.activity

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.crash.alpaca.R
import com.crash.alpaca.fragment.MemoRoomListFragment

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = "AlpacaMemoActivity"
    }

    var backPressCallback: (() -> Boolean)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate : $savedInstanceState")
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.lyFrame, MemoRoomListFragment())
                .commit()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, "onConfigurationChanged : $newConfig")
    }

    override fun onBackPressed() {
        backPressCallback?.invoke()?.also {
            if (!it) {
                super.onBackPressed()
            }
        } ?: super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
