package za.co.botcoin.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateTimeUtils {
    const val DASHED_PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
    const val DASHED_PATTERN_YYYY_MM_DD = "yyyy-MM-dd"
    const val ONE_MINUTE = 1
    const val FIVE_MINUTES = 5

    fun getCurrentDateTime(format: String = DASHED_PATTERN_YYYY_MM_DD_HH_MM_SS): String = SimpleDateFormat(format, Locale.ENGLISH).format(Date())

    private fun getTimeDifference(oldDateTime: String, currentDateTime: String): Long {
        val simpleDateFormat = SimpleDateFormat(DASHED_PATTERN_YYYY_MM_DD_HH_MM_SS, Locale.ENGLISH)
        val oldDate = simpleDateFormat.parse(oldDateTime)
        val currentDate = simpleDateFormat.parse(currentDateTime)
        return if (currentDate != null && oldDate != null) { currentDate.time - oldDate.time } else { 0L }
    }

    fun getCurrentUnixDateTime(): Long = Date().time

    fun getPreviousMidnightUnixDateTime(): Long = Calendar.getInstance(TimeZone.getTimeZone("GMT")).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    fun format(unix: Long, format: String = DASHED_PATTERN_YYYY_MM_DD_HH_MM_SS): String = SimpleDateFormat(format, Locale.ENGLISH).format(Date(unix))

    private fun format(dateTime: String, format: String = DASHED_PATTERN_YYYY_MM_DD_HH_MM_SS): Date? = SimpleDateFormat(format, Locale.ENGLISH).parse(dateTime)

    fun getMinutesFrom(oldDateTime: String, currentDateTime: String) = TimeUnit.MILLISECONDS.toMinutes(getTimeDifference(oldDateTime, currentDateTime))

    fun getYesterdayDateTime(format: String = DASHED_PATTERN_YYYY_MM_DD_HH_MM_SS): String = SimpleDateFormat(format, Locale.ENGLISH).format(Calendar.getInstance().apply {
        add(Calendar.DATE, -1)
    }.time)

    fun getTimeDifferenceInMinutes(dateTime: String): Long {
        val parseDateTime = format(dateTime)
        val currentDateTime = format(getCurrentDateTime())
        return if (parseDateTime != null && currentDateTime != null) { (currentDateTime.time - parseDateTime.time) / (60 * 1000) } else { 0L }
    }

    fun isBeforeDateTime(dateTimeA: String, dateTimeB: String): Boolean {
        val parseDateTimeA = format(dateTimeA)
        val parseDateTimeB = format(dateTimeB)
        return parseDateTimeA != null && parseDateTimeB != null && parseDateTimeA.before(parseDateTimeB)
    }
}