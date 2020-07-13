package com.crash.alpaca.db

import androidx.annotation.WorkerThread
import com.crash.alpaca.Alpaca

//TODO: Study how to make testable code
object AlpacaRepository {

    @WorkerThread
    fun alpacaDao(): AlpacaDao {
        return Alpaca.instance.db.alpacaDao()
    }
}