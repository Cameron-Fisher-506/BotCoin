package za.co.botcoin.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object MathUtils {
    fun precision(value: Double): Double {
        val decimalFormat = DecimalFormat("#.##")
        decimalFormat.decimalFormatSymbols = DecimalFormatSymbols().apply { decimalSeparator = '.' }
        return decimalFormat.format(value).toDouble()
    }

    fun precisionOneDecimal(value: Double): Double {
        val decimalFormat = DecimalFormat("#.#")
        decimalFormat.decimalFormatSymbols = DecimalFormatSymbols().apply { decimalSeparator = '.' }
        return decimalFormat.format(value).toDouble()
    }

    fun percentage(value: Double, percentage: Int): Double = value * (percentage / 100.0f)

    fun calculateMarginPercentage(price: Double, volume: Double, trailingStop: Int, isLowerMargin: Boolean = true): Double {
        val percentage = percentage(price * volume, trailingStop)
        return if (isLowerMargin) {
            precision((price * volume) - precision(percentage))
        } else {
            precision((price * volume) + precision(percentage))
        }
    }

    fun calculateMarginPercentage(price: Double, trailingStop: Int, isLowerMargin: Boolean = true): Double {
        val percentage = percentage(price, trailingStop)
        return if (isLowerMargin) {
            precision(price - precision(percentage))
        } else {
            precision(price + precision(percentage))
        }
    }

    fun reverse(number: Double): Double = number * -1
}