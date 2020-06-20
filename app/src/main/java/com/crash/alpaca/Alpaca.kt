package com.crash.alpaca

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.crash.alpaca.db.AlpacaDatabase

class Alpaca : Application() {
    companion object {
        lateinit var instance: Alpaca
    }

    val context: Context by lazy { applicationContext }

    val db by lazy {
        Room.databaseBuilder(this, AlpacaDatabase::class.java, "alpaca.db").build()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}