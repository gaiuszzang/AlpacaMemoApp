package com.crash.alpaca.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import java.util.*

class MemoRoomFragmentViewModel : ViewModel() {
    private val ioThread = if (Alpaca.DEBUG) Dispatchers.Main else Dispatchers.IO

    var userMsg = MutableLiveData<String>()

    lateinit var context: Context
    var roomId: Int = -1

    fun loadMemo(): LiveData<List<Memo>> {
        return AlpacaRepository.alpacaDao().getMemoList(roomId)
    }

    fun loadMemoRoom(): LiveData<MemoRoom?> {
        return AlpacaRepository.alpacaDao().findMemoRoom(roomId)
    }

    fun updateMemoRoom(memoRoom: MemoRoom) {
        AlpacaRepository.alpacaDao().updateMemoRoom(memoRoom)
    }

    fun addMemo() {
        viewModelScope.launch {
            val content = userMsg.value
            if (content.isNullOrEmpty()) {
                return@launch
            }
            withContext(ioThread) {
                val msgId =  UUID.randomUUID().toString()
                val memo = Memo(msgId, roomId, content, System.currentTimeMillis())
                AlpacaRepository.alpacaDao().insertMemo(memo)
            }
            userMsg.value = ""
        }
    }

    fun copyMemoToClipboard(memo: Memo) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("AlpacaMemo", memo.content)
        clipboard.setPrimaryClip(clip)
    }

    fun removeMemo(memo: Memo) {
        viewModelScope.launch {
            withContext(ioThread) {
                AlpacaRepository.alpacaDao().deleteMemo(memo)
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