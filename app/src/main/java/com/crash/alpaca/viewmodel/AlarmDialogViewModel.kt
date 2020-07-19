package com.crash.alpaca.viewmodel

import androidx.lifecycle.*
import com.crash.alpaca.data.Alarm
import com.crash.alpaca.data.Memo
import com.crash.alpaca.db.AlpacaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlarmDialogViewModel : ViewModel() {
    val timeString = MutableLiveData<String>()
    var memo = MutableLiveData<Memo>()
    var alarm = MutableLiveData<Alarm>()

    fun init(memo: Memo, alarm: Alarm?) {
        this.memo.value = memo
        this.alarm.value = alarm
        timeString.value = alarm?.alarmTime?.toString() ?: System.currentTimeMillis().toString()
    }

    fun saveAlarm(callback: (Alarm?, String?) -> (Unit)) {
        viewModelScope.launch {
            val newAlarmTime = timeString.value!!.toLong()
            val alarm = withContext(Dispatchers.IO) {
                return@withContext AlpacaRepository.saveAlarm(alarm.value, memo.value!!, newAlarmTime)
            }
            callback(alarm, null)
        }
    }
}