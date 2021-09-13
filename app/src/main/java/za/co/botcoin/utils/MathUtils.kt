package za.co.botcoin.utils

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

    fun calcMarginPercentage(price: Double, volume: Double, trailingStop: Int, isLowerMargin: Boolean = true): Double {
        val percentage = percentage(price * volume, trailingStop)
        return if (isLowerMargin) {
            precision((price * volume) - precision(percentage))
        } else {
            precision((price * volume) + precision(percentage))
        }
    }

    fun calcMarginPercentage(price: Double, trailingStop: Int, isLowerMargin: Boolean = true): Double {
        val percentage = percentage(price, trailingStop)
        return if (isLowerMargin) {
            precision(price - precision(percentage))
        } else {
            precision(price + precision(percentage))
        }
    }

    fun reverse(number: Double): Double = number * -1
}