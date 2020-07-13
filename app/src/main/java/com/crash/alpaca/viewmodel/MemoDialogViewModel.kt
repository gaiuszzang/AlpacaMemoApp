package com.crash.alpaca.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.*
import com.crash.alpaca.Alpaca
import com.crash.alpaca.data.Alarm
import com.crash.alpaca.data.Memo
import com.crash.alpaca.db.AlpacaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemoDialogViewModel : ViewModel() {
    var memo = MutableLiveData<Memo>()
    lateinit var alarm: LiveData<Alarm>

    val context = Alpaca.instance.context

    fun init(m: Memo) {
        memo.value = m
        alarm = AlpacaRepository.alpacaDao().getAlarmByMemoId(m.id)
    }

    fun copyMemoToClipboard() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("AlpacaMemo", memo.value?.content)
        clipboard.setPrimaryClip(clip)
    }

    fun removeMemo() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                AlpacaRepository.alpacaDao().deleteMemo(memo.value!!)
            }
        }
    }

    fun removeAlarm() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                AlpacaRepository.alpacaDao().deleteAlarm(alarm.value!!)
            }
        }
    }

}