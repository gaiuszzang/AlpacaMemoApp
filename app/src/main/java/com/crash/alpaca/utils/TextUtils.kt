package com.crash.alpaca.utils

import java.text.SimpleDateFormat
import java.util.*

class TextUtils {
    companion object {

        @JvmStatic
        fun getMemoTimestamp(time:Long): String {
            val memoTimestampFormat = SimpleDateFormat("yy.MM.dd HH:mm:ss", Locale.getDefault())
            val cal = Calendar.getInstance()
            cal.timeInMillis = time
            return memoTimestampFormat.format(cal.time)
        }
    }
}