package com.crash.alpaca.fragment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.crash.alpaca.data.Memo
import com.crash.alpaca.db.AlpacaRepository

class MemoRoomFragmentViewModel : ViewModel() {
    var userMsg = MutableLiveData<String>()
    val memoRoomAdapter = MemoRoomAdapter()
    var plusClickListener: () -> Unit = {}
    var writeClickListener: () -> Unit = {}
    var isListLastPosition = MutableLiveData(false)

    lateinit var context: Context

    fun updateItems(items: List<Memo>) {
        memoRoomAdapter.updateList(items)
    }

    fun setOnItemClickListener(listener: (Memo) -> Unit) {
        memoRoomAdapter.onItemClickListener = listener
    }

    fun setOnSelectModeChangedListener(listener: (Boolean) -> Unit) {
        memoRoomAdapter.onSelectModeChangedListener = listener
    }

    //TODO : should be Refactoring. ViewModel should be don't know Dao.
    fun loadMemo(roomId: Int): LiveData<List<Memo>> {
        return AlpacaRepository.alpacaDao().getMemoList(roomId)
    }

    fun setSelectMode(isOn: Boolean) = memoRoomAdapter.setSelectMode(isOn)

    fun getSelectMode(): Boolean = memoRoomAdapter.getSelectMode()

    fun getSelectItemList(): List<Memo> = memoRoomAdapter.getSelectItemList()

    fun getLayoutManager() : LinearLayoutManager {
        val lyManager = LinearLayoutManager(context)
        //lyManager.reverseLayout = true
        lyManager.stackFromEnd = true
        return lyManager
    }
}