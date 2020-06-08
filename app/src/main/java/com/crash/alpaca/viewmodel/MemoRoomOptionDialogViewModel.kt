package com.crash.alpaca.viewmodel

import androidx.lifecycle.ViewModel
import com.crash.alpaca.db.AlpacaRepository

class MemoRoomOptionDialogViewModel : ViewModel() {

    fun loadMemoRoom(id: Int) = AlpacaRepository.alpacaDao().findMemoRoom(id)
}