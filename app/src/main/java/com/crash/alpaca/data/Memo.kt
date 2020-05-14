package com.crash.alpaca.data

import androidx.room.Entity

@Entity(tableName = "memo", primaryKeys = ["id"])
data class Memo(
    val id: String,
    val content: String
)
