package com.crash.alpaca.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.crash.alpaca.R
import com.crash.alpaca.databinding.ActivityMainBinding
import com.crash.alpaca.fragment.MemoRoomListFragment
import com.crash.alpaca.logd

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "AlpacaMemoActivity"
    }

    var backPressCallback: (() -> Boolean)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logd(TAG, "onCreate : $savedInstanceState")
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.lyFrame, MemoRoomListFragment())
                    .commit()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        logd(TAG, "onConfigurationChanged : $newConfig")
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
