package com.crash.alpaca.data

import androidx.annotation.IntDef
import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * This class represents Entity used by Room Database and is implemented based on below schema
 *
 * CREATE TABLE memoroom (
 *     id INTEGER PRIMARY KEY,
 *     title TEXT NOT NULL,
 *     roomtype INTEGER DEFAULT 0
 *     hidden INTEGER DEFAULT 0
 *     password TEXT
 * )
 */
@Entity(tableName = "memoroom", primaryKeys = ["id"])
data class MemoRoom(
    val id: Int,
    val title: String,
    @ColumnInfo(name = "roomtype")
    @RoomType
    val roomType: Int = TYPE_NORMAL,
    @Visibility
    val hidden: Int = VISIBILITY_SHOW,
    val password: String? = null
) {
    @IntDef(value = [
        TYPE_NORMAL,
        TYPE_DIARY
    ])
    annotation class RoomType

    @IntDef(value = [
        VISIBILITY_HIDDEN,
        VISIBILITY_SHOW
    ])
    annotation class Visibility

    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_DIARY = 1

        const val VISIBILITY_SHOW = 0
        const val VISIBILITY_HIDDEN = 1
    }
}