package com.crash.alpaca.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crash.alpaca.Alpaca
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.db.AlpacaRepository
import kotlinx.coroutines.*

class MemoRoomListFragmentViewModel : ViewModel() {
    private val ioThread = if (Alpaca.DEBUG) Dispatchers.Main else Dispatchers.IO

    fun loadMemoRooms(): LiveData<List<MemoRoom>> {
        return AlpacaRepository.alpacaDao().getAllMemoRoomList()
    }

    fun createNewMemoRoom(title: String, desc: String, callback: (Int) -> Unit) {
        viewModelScope.launch {
            val roomId = withContext(ioThread) {
                val newMemoRoomId = AlpacaRepository.alpacaDao().getMaxMemoRoomId() + 1
                val newMemoRoom = MemoRoom(newMemoRoomId, title, desc,
                    MemoRoom.TYPE_NORMAL, MemoRoom.VISIBILITY_SHOW)
                AlpacaRepository.alpacaDao().insertMemoRoom(newMemoRoom)
                return@withContext newMemoRoomId
            }
            callback(roomId)
        }
    }

    fun removeMemoRoomList(memoRoomList: List<MemoRoom>) {
        viewModelScope.launch(ioThread) {
            memoRoomList.forEach { AlpacaRepository.alpacaDao().deleteMemoRoom(it) }
        }
    }
}
