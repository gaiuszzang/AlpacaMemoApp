package com.crash.alpaca

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.crash.alpaca.db.AlpacaDatabase

class Alpaca : Application() {
    companion object {
        lateinit var instance: Alpaca
        var DEBUG = true // TODO: Need to create settings
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