package com.crash.alpaca.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.core.app.AlarmManagerCompat
import com.crash.alpaca.Alpaca

//TODO : Need more Implementation
object AlarmUtils {
    val context: Context by lazy { Alpaca.instance.context }
    val alarmManager: AlarmManager by lazy { Alpaca.instance.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager }

    fun addAlarmAfterMS(afterMs: Long, alarmId: Int, intent: Intent) {
        val nextTime = SystemClock.elapsedRealtime() + afterMs
        val pIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, AlarmManager.ELAPSED_REALTIME_WAKEUP, nextTime, pIntent)
    }

    fun addAlarmTime(time: Long, alarmId: Int, intent: Intent) {
        val pIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, AlarmManager.RTC_WAKEUP, time, pIntent)
    }
}