package com.crash.alpaca.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.crash.alpaca.R
import com.crash.alpaca.fragment.MemoRoomListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
                .replace(R.id.lyFrame, MemoRoomListFragment())
                .commit()
    }
}
