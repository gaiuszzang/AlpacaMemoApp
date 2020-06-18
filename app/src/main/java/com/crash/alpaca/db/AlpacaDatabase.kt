package com.crash.alpaca.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.crash.alpaca.data.Alarm
import com.crash.alpaca.data.Memo
import com.crash.alpaca.data.MemoRoom

@Database(entities = [MemoRoom::class, Memo::class, Alarm::class], version = 1)
abstract class AlpacaDatabase : RoomDatabase() {
    abstract fun alpacaDao(): AlpacaDao
}