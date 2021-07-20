package za.co.ticker.utils

import java.text.ParseException
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

    private fun parseDateTime(dateTime: String): Date? {
        var toReturn: Date? = null
        try {
            val sdfDate = SimpleDateFormat(DASHED_PATTERN_YYYY_MM_DD_HH_MM_SS, Locale.ENGLISH)
            toReturn = sdfDate.parse(dateTime)
        } catch (e: ParseException) {
            println("Error: ${e.message} " +
                    "Method: parseDateTime " +
                    "Data: $dateTime " +
                    "Date: ${getCurrentDateTime()}")
        }
        return toReturn
    }

    fun getDifferenceDateTimeInMin(dateTime: String): Long {
        var toReturn: Long = 0L

        val parseDateTime = parseDateTime(dateTime)
        val currentDateTime = parseDateTime(getCurrentDateTime())
        if (parseDateTime != null && currentDateTime != null) {
            val difference = currentDateTime.time - parseDateTime.time
            toReturn = difference / (60 * 1000)
        }

        return toReturn
    }
}