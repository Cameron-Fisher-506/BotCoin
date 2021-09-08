package za.co.botcoin.utils

import za.co.botcoin.model.models.Candle
import java.util.*

class SimpleMovingAverage(var period: Int) {
    val dataSet: Queue<Candle> = LinkedList()
    val averages: Queue<Double> = LinkedList()
    private var sum: Double = 0.0

    fun calculateSma(candles: List<Candle>) {
        if (candles.isNotEmpty()) {
            candles.map { candle -> addRemoveCandle(candle) }
        }
    }

    private fun addRemoveCandle(candle: Candle) {
        if (dataSet.isNotEmpty() && dataSet.size >= period) {
            sum -= dataSet.remove().close.toDouble()
        }
        dataSet.add(candle)
        sum += candle.close.toDouble()
        calculateAverage()
    }

    private fun calculateAverage() {
        if (dataSet.isNotEmpty() && dataSet.size >= period) {
            if (averages.isNotEmpty() && averages.size >= 20) {
                averages.remove()
            }
            averages.add(sum/period)
        }
    }

    fun isPriceOnLine(currentPrice: Double): Boolean {
        if (averages.isNotEmpty()) {
            for (i in 0 until averages.size) {
                if (currentPrice in averages.elementAt(i)..averages.elementAt(i+1)) {
                    return true
                }
            }
        }
        return false
    }
}