package za.co.botcoin.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object MathUtils {
    @JvmStatic
    fun precision(value: Double?): Double? {
        var toReturn: Double? = null
        if (value != null) {
            val dfs = DecimalFormatSymbols()
            dfs.decimalSeparator = '.'
            val decimalFormat = DecimalFormat("#.##")
            decimalFormat.decimalFormatSymbols = dfs
            toReturn = java.lang.Double.valueOf(decimalFormat.format(value))
        }
        return toReturn
    }

    @JvmStatic
    fun percentage(value: Double?, percentage: Int?): Double? {
        var toReturn: Double? = null
        if (value != null && percentage != null) {
            toReturn = value * (percentage / 100.0f)
        }
        return toReturn
    }
}