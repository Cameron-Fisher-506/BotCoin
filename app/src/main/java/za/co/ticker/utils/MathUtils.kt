package za.co.ticker.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object MathUtils {
    fun precision(value: Double): Double {
        val decimalFormatSymbols = DecimalFormatSymbols()
        decimalFormatSymbols.decimalSeparator = '.'
        val decimalFormat = DecimalFormat("#.##")
        decimalFormat.decimalFormatSymbols = decimalFormatSymbols
        return decimalFormat.format(value).toDouble()
    }

    fun percentage(value: Double, percentage: Int): Double = value * (percentage / 100.0f)
}