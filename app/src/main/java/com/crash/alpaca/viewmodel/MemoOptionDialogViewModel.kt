package com.crash.alpaca.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crash.alpaca.data.Alarm
import com.crash.alpaca.data.Memo
import com.crash.alpaca.db.AlpacaRepository
import kotlinx.coroutines.launch

class MemoOptionDialogViewModel : ViewModel() {
    var memo = MutableLiveData<Memo>()
    lateinit var alarm : LiveData<Alarm>

    fun init(m: Memo) {
        memo.value = m
        alarm = AlpacaRepository.alpacaDao().getAlarm(m.id)
        viewModelScope.launch {
            AlpacaRepository.alpacaDao()
        }
    }
}