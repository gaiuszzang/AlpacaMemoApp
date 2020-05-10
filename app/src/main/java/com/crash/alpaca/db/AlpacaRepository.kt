package com.crash.alpaca.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crash.alpaca.Alpaca
import com.crash.alpaca.data.MemoRoom

//TODO: Study how to make testable code
object AlpacaRepository {

    var debugDao = if (Alpaca.DEBUG) (object: AlpacaDao {
        val memoRoomList : ArrayList<MemoRoom> = arrayListOf(
            MemoRoom(0, "Test Room 0", "This is Description of Room 0"),
            MemoRoom(1, "Test Room 1", "This is Description of Room 1"),
            MemoRoom(2, "Test Room 2", "This is Description of Room 2"),
            MemoRoom(3, "Test Room 3", "This is Description of Room 3"),
            MemoRoom(4, "Test Room 4", "This is Description of Room 4"),
            MemoRoom(5, "Test Room 5", "This is Description of Room 5")
        )
        val liveDataList : MutableLiveData<List<MemoRoom>> = MutableLiveData(memoRoomList)

        override fun insertMemoRoom(memoRoom: MemoRoom) {
            memoRoomList.add(memoRoom)
            liveDataList.value = memoRoomList
        }

        override fun deleteMemoRoom(memoRoom: MemoRoom) {
            memoRoomList.remove(memoRoom)
            liveDataList.value = memoRoomList
        }

        override fun deleteMemoRoom(id: Int) {
            memoRoomList.removeIf { it.id == id }
            liveDataList.value = memoRoomList
        }

        override fun findMemoRoom(id: Int): LiveData<MemoRoom?> {
            TODO("Not yet implemented")
        }

        override fun getAllMemoRoomList(): LiveData<List<MemoRoom>> {
            return liveDataList
        }

        override fun getMemoRoomList(isHidden: Int): LiveData<List<MemoRoom>> {
            TODO("Not yet implemented")
        }

        override fun getMaxMemoRoomId(): Int {
            var maxId: Int = 0
            for (memoRoom in memoRoomList) {
                if (maxId < memoRoom.id) {
                    maxId = memoRoom.id
                }
            }
            return maxId
        }
    }) else null

    @WorkerThread
    fun alpacaDao(): AlpacaDao {
        return if (!Alpaca.DEBUG) {
            Alpaca.instance.db.alpacaDao()
        } else {
            debugDao
        }
    }
}