package com.crash.alpaca.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.crash.alpaca.R
import com.crash.alpaca.fragment.MemoRoomListFragment

class MainActivity : AppCompatActivity() {
    interface IBackPressCallback {
        fun onBackPressed(): Boolean
    }

    var backPressCallback: IBackPressCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
                .replace(R.id.lyFrame, MemoRoomListFragment())
                .commit()
    }

    override fun onBackPressed() {
        if (backPressCallback == null || !backPressCallback!!.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun addBackPressCallback(cb: IBackPressCallback) {
        backPressCallback = cb
    }
}
