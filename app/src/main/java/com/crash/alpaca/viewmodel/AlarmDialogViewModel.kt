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
                lateinit var newAlarm: Alarm
                if (alarm.value != null) {
                    //Update Alarm
                    newAlarm = alarm.value!!.copy(alarmTime = newAlarmTime)
                    AlpacaRepository.alpacaDao().updateAlarm(newAlarm)
                } else {
                    //Create Alarm
                    val newAlarmId = AlpacaRepository.alpacaDao().getMaxAlarmId() + 1
                    newAlarm = Alarm(id = newAlarmId, memoId = memo.value!!.id, alarmTime = newAlarmTime)
                    AlpacaRepository.alpacaDao().insertAlarm(newAlarm)
                }
                return@withContext newAlarm
            }
            callback(alarm, null)
        }
    }
}