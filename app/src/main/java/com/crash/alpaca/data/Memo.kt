package com.crash.alpaca.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "memo",
    primaryKeys = ["id"],
    foreignKeys = [
        ForeignKey(
            entity = MemoRoom::class,
            parentColumns = ["id"],
            childColumns = ["room_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Memo(
    val id: String,
    @ColumnInfo(name = "room_id")
    val roomId: Int,
    val content: String,
    val time: Long
)
