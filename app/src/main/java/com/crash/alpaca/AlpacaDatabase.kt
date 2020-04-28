package com.crash.alpaca

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MemoRoom::class], version = 1)
abstract class AlpacaDatabase : RoomDatabase() {
    abstract fun alpacaDao(): AlpacaDao
}