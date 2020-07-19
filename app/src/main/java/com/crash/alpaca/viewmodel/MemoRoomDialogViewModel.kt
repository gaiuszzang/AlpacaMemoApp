package com.crash.alpaca.viewmodel

import androidx.lifecycle.*
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.db.AlpacaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemoRoomDialogViewModel : ViewModel() {
    var memoRoomId = MutableLiveData(-1)
    var title = MutableLiveData<String>("")
    var typeNormal = MutableLiveData<Boolean>(true)
    var typeDiary = MutableLiveData<Boolean>(false)
    var isHidden    = MutableLiveData<Boolean>(false)

    var memoRoomObserver = Observer<MemoRoom?> {
        memoRoomId.value = it?.id
        title.value = it?.title
        typeNormal.value = (it?.roomType == MemoRoom.TYPE_NORMAL)
        typeDiary.value = (it?.roomType == MemoRoom.TYPE_DIARY)
        isHidden.value = (it?.hidden == MemoRoom.VISIBILITY_HIDDEN)
    }

    fun loadMemoRoom(id: Int) {
        val memoRoom = AlpacaRepository.getMemoRoom(id)
        var memoRoomObserver = Observer<MemoRoom?> {
            memoRoomId.value = it?.id
            title.value = it?.title
            typeNormal.value = (it?.roomType == MemoRoom.TYPE_NORMAL)
            typeDiary.value = (it?.roomType == MemoRoom.TYPE_DIARY)
            isHidden.value = (it?.hidden == MemoRoom.VISIBILITY_HIDDEN)
            memoRoom.removeObserver(memoRoomObserver)
        }
        memoRoom.observeForever(memoRoomObserver)
    }


    fun saveMemoRoom(callback: (MemoRoom?, String?) -> (Unit)) {
        viewModelScope.launch {
            if (title.value!!.isEmpty()) {
                callback(null, "Please Input Title")
                return@launch
            }
            val memoRoom = withContext(Dispatchers.IO) {
                val roomType = if (typeNormal.value!!) MemoRoom.TYPE_NORMAL else MemoRoom.TYPE_DIARY
                val hidden = if (isHidden.value!!) MemoRoom.VISIBILITY_HIDDEN else MemoRoom.VISIBILITY_SHOW
                return@withContext AlpacaRepository.saveMemoRoom(memoRoomId.value!!, title.value!!, roomType, hidden)
            }
            callback(memoRoom, null)
        }
    }

}