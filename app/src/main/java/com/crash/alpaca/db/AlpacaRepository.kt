package com.crash.alpaca.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crash.alpaca.Alpaca
import com.crash.alpaca.data.MemoRoom

//TODO: Study how to make testable code
object AlpacaRepository {

    @WorkerThread
    fun alpacaDao(): AlpacaDao {
        return if (!Alpaca.DEBUG) {
            Alpaca.instance.db.alpacaDao()
        } else {
            object : AlpacaDao {
                override fun insertMemoRoom(memoRoom: MemoRoom) {
                    TODO("Not yet implemented")
                }

                override fun deleteMemoRoom(memoRoom: MemoRoom) {
                    TODO("Not yet implemented")
                }

                override fun findMemoRoom(id: Int): LiveData<MemoRoom?> {
                    TODO("Not yet implemented")
                }

                override fun getAllMemoRoomList(): LiveData<List<MemoRoom>> {
                    return MutableLiveData(listOf(
                        MemoRoom(0, "Test Room 0", "This is Description of Room 0"),
                        MemoRoom(1, "Test Room 1", "This is Description of Room 1"),
                        MemoRoom(2, "Test Room 2", "This is Description of Room 2"),
                        MemoRoom(3, "Test Room 3", "This is Description of Room 3"),
                        MemoRoom(4, "Test Room 4", "This is Description of Room 4"),
                        MemoRoom(5, "Test Room 5", "This is Description of Room 5")
                    ))
                }

                override fun getMemoRoomList(isHidden: Int): LiveData<List<MemoRoom>> {
                    TODO("Not yet implemented")
                }
            }
        }
    }
}