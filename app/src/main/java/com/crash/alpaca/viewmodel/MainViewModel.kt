package com.crash.alpaca.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.db.AlpacaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

class MainViewModel : ViewModel() {

    val showMemoEvent: LiveData<Int> = SingleEvent()
    val selectMode: LiveData<Boolean> = MutableLiveData(false)
    val selectedRooms: MutableLiveData<MutableList<MemoRoom>> = MutableLiveData(mutableListOf())

    fun loadMemoRooms(): LiveData<List<MemoRoom>> {
        return AlpacaRepository.alpacaDao().getAllMemoRoomList()
    }

    fun removeMemoRooms() {
        viewModelScope.launch(Dispatchers.Default) {
            if (selectedRooms.value!!.isNotEmpty()) {
                withContext(Dispatchers.IO) {
                    AlpacaRepository.removeMemoRooms(selectedRooms.value!!)
                }
                selectedRooms.postValue(mutableListOf())
                setSelectMode(false)
            }
        }
    }

    fun onBackPress() = if (selectMode.value == true) {
        selectedRooms.postValue(mutableListOf())
        (selectMode as MutableLiveData).postValue(false)
        true
    } else {
        false
    }

    fun onClickMemoRoom(memoRoom: MemoRoom) {
        if (selectMode.value == true) {
            if (selectedRooms.value!!.removeIf { it == memoRoom }) {
                if (selectedRooms.value!!.isEmpty()) {
                    setSelectMode(false)
                }
            } else {
                selectedRooms.value!!.add(memoRoom)
            }
            selectedRooms.postValue(selectedRooms.value)
        } else {
            (showMemoEvent as MutableLiveData).postValue(memoRoom.id)
        }
    }

    fun onLongClickMemoRoom(memoRoom: MemoRoom) {
        if (selectMode.value == false) {
            setSelectMode(true)
            viewModelScope.launch(Dispatchers.Main) {
                onClickMemoRoom(memoRoom)
            }
        }
    }

    private fun setSelectMode(mode: Boolean) {
        (selectMode as MutableLiveData).postValue(mode)
    }

    private class SingleEvent<T> : MutableLiveData<T>() {
        private var consumed = AtomicBoolean()

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, Observer {
                if (consumed.compareAndSet(false, true)) {
                    observer.onChanged(it)
                }
            })
        }

        override fun setValue(value: T) {
            consumed.set(false)
            super.setValue(value)
        }
    }
}
