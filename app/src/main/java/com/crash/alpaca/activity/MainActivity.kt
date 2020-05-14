package com.crash.alpaca.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.crash.alpaca.R
import com.crash.alpaca.fragment.MemoRoomListFragment

class MainActivity : AppCompatActivity() {

    var backPressCallback: (() -> Boolean)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
                .replace(R.id.lyFrame, MemoRoomListFragment())
                .commit()
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
