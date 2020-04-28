package com.crash.alpaca

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AlpacaDao {
    @Insert
    fun insertMemoRoom(memoRoom: MemoRoom)

    @Delete
    fun deleteMemoRoom(memoRoom: MemoRoom)

    @Query("SELECT * FROM memoroom WHERE id = :id")
    fun findMemoRoom(id: Int): MemoRoom?

    @Query("SELECT * FROM memoroom")
    fun getAllMemoRoom(): List<MemoRoom>
}