package com.crash.alpaca.fragment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.crash.alpaca.Alpaca
import com.crash.alpaca.data.Memo
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.db.AlpacaRepository
import kotlinx.coroutines.*
import java.util.*

class MemoRoomFragmentViewModel : ViewModel() {
    var userMsg = MutableLiveData<String>()

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private val ioThread = if (Alpaca.DEBUG) Dispatchers.Main else Dispatchers.IO

    lateinit var context: Context

    fun loadMemo(roomId: Int): LiveData<List<Memo>> {
        return AlpacaRepository.alpacaDao().getMemoList(roomId)
    }

    fun loadMemoRoom(roomId: Int): LiveData<MemoRoom?> {
        return AlpacaRepository.alpacaDao().findMemoRoom(roomId)
    }

    fun addMemo(roomId: Int) {
        scope.launch {
            val content = userMsg.value?: ""
            if (!content.equals("")) {
                val msgId =  UUID.randomUUID().toString()
                async(ioThread) {
                    val memo = Memo(msgId, roomId, content, System.currentTimeMillis())
                    AlpacaRepository.alpacaDao().insertMemo(memo)
                }.await()
                userMsg.value = "";
            }
        }
    }

    fun getLayoutManager() : LinearLayoutManager {
        val lyManager = LinearLayoutManager(context)
        //lyManager.reverseLayout = true
        lyManager.stackFromEnd = true
        return lyManager
    }
}