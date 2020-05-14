package com.crash.alpaca.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.db.AlpacaRepository

class MemoRoomListFragmentViewModel : ViewModel() {

    val memoRoomAdapter = MemoRoomListAdapter()

    fun updateItems(items: List<MemoRoom>) {
        memoRoomAdapter.updateList(items)
    }

    fun setOnItemClickListener(listener: (MemoRoom) -> Unit) {
        memoRoomAdapter.onItemClickListener = listener
    }

    fun setOnSelectModeChangedListener(listener: (Boolean) -> Unit) {
        memoRoomAdapter.onSelectModeChangedListener = listener
    }

    fun loadMemoRooms(): LiveData<List<MemoRoom>> {
        return AlpacaRepository.alpacaDao().getAllMemoRoomList()
    }

    fun setSelectMode(isOn: Boolean) = memoRoomAdapter.setSelectMode(isOn)

    fun getSelectMode(): Boolean = memoRoomAdapter.getSelectMode()

    fun getSelectItemList(): List<MemoRoom> = memoRoomAdapter.getSelectItemList()
}

