package com.crash.alpaca.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.crash.alpaca.data.Memo
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.db.AlpacaRepository
import java.util.*

class MemoRoomFragmentViewModel : ViewModel() {
    var userMsg = MutableLiveData<String>()

    lateinit var context: Context
    var roomId: Int = -1

    fun loadMemo(): LiveData<List<Memo>> {
        return AlpacaRepository.alpacaDao().getMemoList(roomId)
    }

    fun loadMemoRoom(): LiveData<MemoRoom?> {
        return AlpacaRepository.alpacaDao().findMemoRoom(roomId)
    }

    fun addMemo(content: String) {
        if (!content.equals("")) {
            val msgId =  UUID.randomUUID().toString()
            val memo = Memo(msgId, roomId, content, System.currentTimeMillis())
            AlpacaRepository.alpacaDao().insertMemo(memo)
        }
    }

    fun getLayoutManager() : LinearLayoutManager {
        val lyManager = LinearLayoutManager(context)
        //lyManager.reverseLayout = true
        lyManager.stackFromEnd = true
        return lyManager
    }
}