package com.crash.alpaca.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.crash.alpaca.Alpaca
import com.crash.alpaca.data.Alarm
import com.crash.alpaca.data.Memo
import com.crash.alpaca.data.MemoRoom
import java.util.*

//TODO: Study how to make testable code
object AlpacaRepository {

    //private : Blocking Access Dao Directly
    @WorkerThread
    private fun alpacaDao(): AlpacaDao {
        return Alpaca.instance.db.alpacaDao()
    }

    //Memo Methods
    fun saveMemoRoom(memoRoomId: Int, title:String, @MemoRoom.RoomType roomType: Int, @MemoRoom.Visibility hidden: Int) : MemoRoom {
        lateinit var memoRoom: MemoRoom
        if (memoRoomId == -1) {
            memoRoom = MemoRoom(alpacaDao().getMaxMemoRoomId() + 1, title, roomType, hidden)
            alpacaDao().insertMemoRoom(memoRoom)
        } else {
            memoRoom = MemoRoom(memoRoomId, title, roomType, hidden)
            alpacaDao().updateMemoRoom(memoRoom)
        }
        return memoRoom
    }

    fun removeMemoRooms(memoRoomList: List<MemoRoom>) {
        alpacaDao().deleteMemoRoom(memoRoomList)
    }

    fun getMemoRoomList(showAll: Boolean = true): LiveData<List<MemoRoom>> {
        if (showAll)
            return alpacaDao().getAllMemoRoomList()
        else
            return alpacaDao().getMemoRoomList(MemoRoom.VISIBILITY_SHOW) //Only Show noHidden
    }

    fun getMemoRoom(id: Int): LiveData<MemoRoom?> {
        return alpacaDao().findMemoRoom(id)
    }

    //Memo Methods
    fun getMemoList(roomId: Int): LiveData<List<Memo>> {
        return alpacaDao().getMemoList(roomId)
    }

    fun addMemo(roomId: Int, content: String) {
        val msgId =  UUID.randomUUID().toString()
        val memo = Memo(msgId, roomId, content, System.currentTimeMillis())
        alpacaDao().insertMemo(memo)
    }

    fun removeMemo(memo: Memo) {
        alpacaDao().deleteMemo(memo)
    }

    //Alarm Methods
    fun saveAlarm(alarm: Alarm?, memo: Memo, newAlarmTime: Long): Alarm {
        lateinit var newAlarm: Alarm
        if (alarm != null) {
            //Update Alarm
            newAlarm = alarm.copy(alarmTime = newAlarmTime)
            alpacaDao().updateAlarm(newAlarm)
        } else {
            //Create Alarm
            val newAlarmId = alpacaDao().getMaxAlarmId() + 1
            newAlarm = Alarm(id = newAlarmId, memoId = memo.id, alarmTime = newAlarmTime)
            alpacaDao().insertAlarm(newAlarm)
        }
        return newAlarm
    }

    fun removeAlarm(alarm: Alarm) {
        alpacaDao().deleteAlarm(alarm)
    }

    fun getAlarmByMemoId(memoId: String): LiveData<Alarm> {
        return alpacaDao().getAlarmByMemoId(memoId)
    }
}