package com.crash.alpaca.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.crash.alpaca.data.MemoRoom

@Dao
interface AlpacaDao {
    @Insert
    fun insertMemoRoom(memoRoom: MemoRoom)

    @Delete
    fun deleteMemoRoom(memoRoom: MemoRoom)

    @Query("SELECT * FROM memoroom WHERE id = :id")
    fun findMemoRoom(id: Int): LiveData<MemoRoom?>

    @Query("SELECT * FROM memoroom")
    fun getAllMemoRoomList(): LiveData<List<MemoRoom>>

    @Query("SELECT * FROM memoroom WHERE hidden = :isHidden")
    fun getMemoRoomList(@MemoRoom.Visibility isHidden: Int): LiveData<List<MemoRoom>>
}