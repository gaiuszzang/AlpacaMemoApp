package com.crash.alpaca

import com.crash.alpaca.utils.TextUtils
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.text.SimpleDateFormat

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TextUtilsUnitTest {

    @Before
    fun setUp() {
        //Nothing
    }

    @Test
    fun timeStampTest() {
        val time1 = SimpleDateFormat("yyyy.MM.dd HH:mm").parse("2020.06.04 10:30").time
        val timeText = TextUtils.getMemoTimestamp(time1)
        assertEquals("20.06.04 10:30:00", timeText)
    }
}
