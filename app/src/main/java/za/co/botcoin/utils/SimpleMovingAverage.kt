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
            var m = 0.0
            var c = 0.0
            for (i in 0 until averages.size-1) {
                m = StraightLineFormulaUtils.calculateGradient(i+1, i, averages.elementAt(i+1), averages.elementAt(i))
                c = StraightLineFormulaUtils.calculateConstant(i+1, averages.elementAt(i+1), m)
                if (StraightLineFormulaUtils.isPointOnLine(i, currentPrice, m, c)) {
                    return true
                }
            }
        }
        return false
    }
}