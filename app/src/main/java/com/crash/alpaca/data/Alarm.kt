package com.crash.alpaca.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "alarm",
    primaryKeys = ["id"],
    foreignKeys = [
        ForeignKey(
            entity = Memo::class,
            parentColumns = ["id"],
            childColumns = ["memo_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Alarm(
    val id: Int,
    @ColumnInfo(name = "memo_id")
    val memoId: String,
    val alarmTime: Long)
