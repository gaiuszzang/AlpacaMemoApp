package com.crash.alpaca.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crash.alpaca.data.Memo

class MemoOptionDialogViewModel : ViewModel() {
    var memo = MutableLiveData<Memo>()

    fun init(m: Memo) {
        memo.value = m
    }
}