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
    var desc  = MutableLiveData<String>("")
    var typeNormal = MutableLiveData<Boolean>(true)
    var typeDiary = MutableLiveData<Boolean>(false)
    var isHidden    = MutableLiveData<Boolean>(false)

    fun loadMemoRoom(owner: LifecycleOwner, id: Int) {
        AlpacaRepository.alpacaDao().findMemoRoom(id).observe(owner, Observer {
            memoRoomId.value = it?.id
            title.value = it?.title
            desc.value = it?.description
            typeNormal.value = (it?.roomType == MemoRoom.TYPE_NORMAL)
            typeDiary.value = (it?.roomType == MemoRoom.TYPE_DIARY)
            isHidden.value = (it?.hidden == MemoRoom.VISIBILITY_HIDDEN)
        })
    }

    fun saveMemoRoom(callback: (MemoRoom?, String?) -> (Unit)) {
        viewModelScope.launch {
            if (title.value!!.isEmpty() || desc.value!!.isEmpty()) {
                callback(null, "Please Input Title & Description")
                return@launch
            }
            val memoRoom = withContext(Dispatchers.IO) {
                lateinit var memoRoom : MemoRoom
                val roomType = if (typeNormal.value!!) MemoRoom.TYPE_NORMAL else MemoRoom.TYPE_DIARY
                val hidden = if (isHidden.value!!) MemoRoom.VISIBILITY_HIDDEN else MemoRoom.VISIBILITY_SHOW

                if (memoRoomId.value == -1) {
                    val newMemoRoomId = AlpacaRepository.alpacaDao().getMaxMemoRoomId() + 1
                    memoRoom = MemoRoom(newMemoRoomId, title.value!!, desc.value!!, roomType, hidden)
                    AlpacaRepository.alpacaDao().insertMemoRoom(memoRoom)
                } else {
                    memoRoom = MemoRoom(memoRoomId.value!!, title.value!!, desc.value!!, roomType, hidden)
                    AlpacaRepository.alpacaDao().updateMemoRoom(memoRoom)
                }
                return@withContext memoRoom
            }
            callback(memoRoom, null)
        }
    }

}