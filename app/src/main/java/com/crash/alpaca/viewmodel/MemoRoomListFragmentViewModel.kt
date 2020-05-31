package com.crash.alpaca.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.db.AlpacaRepository

class MemoRoomListFragmentViewModel : ViewModel() {

    fun loadMemoRooms(): LiveData<List<MemoRoom>> {
        return AlpacaRepository.alpacaDao().getAllMemoRoomList()
    }

    fun createNewMemoRoom(title: String, desc: String): Int {
        val newMemoRoomId = AlpacaRepository.alpacaDao().getMaxMemoRoomId() + 1
        val newMemoRoom = MemoRoom(newMemoRoomId, title, desc,
                MemoRoom.TYPE_NORMAL, MemoRoom.VISIBILITY_SHOW)
        AlpacaRepository.alpacaDao().insertMemoRoom(newMemoRoom)
        return newMemoRoomId
    }

    fun removeMemoRoom(memoRoom: MemoRoom) {
        AlpacaRepository.alpacaDao().deleteMemoRoom(memoRoom)
    }
}
