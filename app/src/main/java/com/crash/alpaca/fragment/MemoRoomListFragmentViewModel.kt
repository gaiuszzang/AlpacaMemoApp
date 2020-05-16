package com.crash.alpaca.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.db.AlpacaRepository

class MemoRoomListFragmentViewModel : ViewModel() {

    val memoRoomListAdapter = MemoRoomListAdapter()

    fun updateItems(items: List<MemoRoom>) {
        memoRoomListAdapter.updateList(items)
    }

    fun setOnItemClickListener(listener: (MemoRoom) -> Unit) {
        memoRoomListAdapter.onItemClickListener = listener
    }

    fun setOnSelectModeChangedListener(listener: (Boolean) -> Unit) {
        memoRoomListAdapter.onSelectModeChangedListener = listener
    }

    //TODO : should be Refactoring. ViewModel should be don't know Dao.
    fun loadMemoRooms(): LiveData<List<MemoRoom>> {
        return AlpacaRepository.alpacaDao().getAllMemoRoomList()
    }

    fun setSelectMode(isOn: Boolean) = memoRoomListAdapter.setSelectMode(isOn)

    fun getSelectMode(): Boolean = memoRoomListAdapter.getSelectMode()

    fun getSelectItemList(): List<MemoRoom> = memoRoomListAdapter.getSelectItemList()
}

