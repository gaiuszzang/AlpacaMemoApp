package com.crash.alpaca.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.crash.alpaca.data.Alarm
import com.crash.alpaca.data.Memo
import com.crash.alpaca.data.MemoRoom

@Dao
interface AlpacaDao {
    @Insert
    fun insertMemoRoom(memoRoom: MemoRoom)

    @Delete
    fun deleteMemoRoom(memoRoom: MemoRoom)

    @Update
    fun updateMemoRoom(memoRoom: MemoRoom)

    //@Query("DELETE FROM memoroom WHERE id = :id")
    //fun deleteMemoRoom(id: Int)

    @Query("SELECT * FROM memoroom WHERE id = :id")
    fun findMemoRoom(id: Int): LiveData<MemoRoom?>

    @Query("SELECT * FROM memoroom")
    fun getAllMemoRoomList(): LiveData<List<MemoRoom>>

    @Query("SELECT * FROM memoroom WHERE hidden = :isHidden")
    fun getMemoRoomList(@MemoRoom.Visibility isHidden: Int): LiveData<List<MemoRoom>>

    @Query("SELECT MAX(id) FROM memoroom")
    fun getMaxMemoRoomId(): Int

    @Insert
    fun insertMemo(memo: Memo)

    @Delete
    fun deleteMemo(memo: Memo)

    @Query("SELECT * FROM memo WHERE room_id = :roomId ORDER BY time ASC")
    fun getMemoList(roomId: Int): LiveData<List<Memo>>

    @Query("SELECT * FROM alarm WHERE memo_id = :memoId")
    fun getAlarm(memoId: String): LiveData<Alarm>

}