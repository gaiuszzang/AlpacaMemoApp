package com.crash.alpaca.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MemoRoomOptionDialogViewModel : ViewModel() {
    var roomTitle = MutableLiveData("")
    var roomDesc = MutableLiveData("")
}