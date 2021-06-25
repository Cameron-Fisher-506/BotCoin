package za.co.botcoin.utils

import java.text.SimpleDateFormat
import java.util.*

object DTUtils {
    fun getCurrentDateTime(): String {
        var toReturn = ""

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.ENGLISH)
        var now = Date()
        val calendar = Calendar.getInstance()
        calendar.time = now
        now = calendar.time
        toReturn = simpleDateFormat.format(now)

        return toReturn
    }

    private fun parseDateTime(dateTime: String): Date? {
        var toReturn: Date? = null
        try {
            val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
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
        var toReturn: Long = 0
        val parseDateTime = parseDateTime(dateTime)
        val currentDateTime = parseDateTime(getCurrentDateTime())
        val difference = currentDateTime!!.time - parseDateTime!!.time
        toReturn = difference / (60 * 1000)
        return toReturn
    }
}