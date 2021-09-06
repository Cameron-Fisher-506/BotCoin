package za.co.botcoin.utils

import za.co.botcoin.model.models.Candle
import java.util.*

class SimpleMovingAverage(var period: Int) {
    val dataSet: Queue<Candle> = LinkedList()
    val sma: Queue<Double> = LinkedList()
    var sum: Double = 0.0

    fun calcSMA(candles: List<Candle>) {
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
            if (sma.isNotEmpty() && sma.size >= 20) {
                sma.remove()
            }
            sma.add(sum/period)
        }
    }
}