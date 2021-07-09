package za.co.botcoin.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateTimeUtils {
    const val DASHED_PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
    const val ONE_MINUTE = 1

    fun getCurrentDateTime(format: String = DASHED_PATTERN_YYYY_MM_DD_HH_MM_SS): String = SimpleDateFormat(format, Locale.ENGLISH).format(Date())

    private fun differenceInTime(oldDateTime: String, currentDateTime: String): Long {
        var toReturn: Long = 0L

        val simpleDateFormat = SimpleDateFormat(DASHED_PATTERN_YYYY_MM_DD_HH_MM_SS, Locale.ENGLISH)
        val oldDate = simpleDateFormat.parse(oldDateTime)
        val currentDate = simpleDateFormat.parse(currentDateTime)

        if (oldDate != null && currentDate != null) {
            toReturn = currentDate.time - oldDate.time
        }

        return toReturn
    }

    fun differenceInMinutes(oldDateTime: String, currentDateTime: String) = TimeUnit.MILLISECONDS.toMinutes(differenceInTime(oldDateTime, currentDateTime))

    fun getCurrentDateTime(): String {
        val simpleDateFormat = SimpleDateFormat(DASHED_PATTERN_YYYY_MM_DD_HH_MM_SS, Locale.ENGLISH)
        var now = Date()
        val calendar = Calendar.getInstance()
        calendar.time = now
        now = calendar.time
        return simpleDateFormat.format(now)
    }

    private fun parseDateTime(dateTime: String): Date? {
        var toReturn: Date? = null
        try {
            val sdfDate = SimpleDateFormat(DASHED_PATTERN_YYYY_MM_DD_HH_MM_SS, Locale.ENGLISH)
            toReturn = sdfDate.parse(dateTime)
        } catch (e: Exception) {
            println("Error: ${e.message} " +
                    "Method: parseDateTime " +
                    "Data: $dateTime " +
                    "Date: ${GeneralUtils.getCurrentDateTime()}")
        }
        return toReturn
    }

    fun getDifferenceDateTimeInMin(dateTime: String): Long {
        val parseDateTime = parseDateTime(dateTime)
        val currentDateTime = parseDateTime(getCurrentDateTime())
        val difference = currentDateTime!!.time - parseDateTime!!.time
        return  difference / (60 * 1000)
    }
}