package com.crash.alpaca.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.crash.alpaca.Alpaca
import com.crash.alpaca.data.Memo
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.db.AlpacaRepository
import kotlinx.coroutines.*

class MemoRoomFragmentViewModel : ViewModel() {

    var userMsg = MutableLiveData<String>()
    val context = Alpaca.instance.context
    var roomId: Int = -1

    fun loadMemo(): LiveData<List<Memo>> {
        return AlpacaRepository.getMemoList(roomId)
    }

    fun loadMemoRoom(): LiveData<MemoRoom?> {
        return AlpacaRepository.getMemoRoom(roomId)
    }

    fun addMemo() {
        viewModelScope.launch {
            val content = userMsg.value
            if (content.isNullOrEmpty()) {
                return@launch
            }
            withContext(Dispatchers.IO) {
                AlpacaRepository.addMemo(roomId, content)
            }
            userMsg.value = ""
        }
    }

    fun getLayoutManager() : LinearLayoutManager {
        val lyManager = LinearLayoutManager(context)
        //lyManager.reverseLayout = true
        lyManager.stackFromEnd = true
        return lyManager
    }
}