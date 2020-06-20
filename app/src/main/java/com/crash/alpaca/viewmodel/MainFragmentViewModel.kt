package com.crash.alpaca.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crash.alpaca.Alpaca
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.db.AlpacaRepository
import kotlinx.coroutines.*

class MainFragmentViewModel : ViewModel() {
    fun loadMemoRooms(): LiveData<List<MemoRoom>> {
        return AlpacaRepository.alpacaDao().getAllMemoRoomList()
    }

    fun removeMemoRoomList(memoRoomList: List<MemoRoom>) {
        viewModelScope.launch(Dispatchers.IO) {
            memoRoomList.forEach { AlpacaRepository.alpacaDao().deleteMemoRoom(it) }
        }
    }
}
