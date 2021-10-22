package za.co.botcoin.utils

import android.util.Log
import za.co.botcoin.model.models.Candle
import java.util.*

class SimpleMovingAverage(var period: Int) {
    private val dataSet: Queue<Candle> = LinkedList()
    val averages: Queue<Double> = LinkedList()
    private var sum: Double = 0.0

    fun calculateSma(candles: List<Candle>) {
        if (dataSet.isNotEmpty() && dataSet.size >= period) {
            val candleExists = dataSet.last().timestamp == candles.last().timestamp
            if (!candleExists) {
                sum -= dataSet.remove().close.toDouble()
                if (averages.isNotEmpty() && averages.size >= period) { averages.remove() }

                dataSet.add(candles.last())
                sum += candles.last().close.toDouble()
                averages.add(MathUtils.precision(sum/period))
            }
        } else {
            if (candles.isNotEmpty()) {
                candles.map { candle ->
                    dataSet.add(candle)
                    sum += candle.close.toDouble()
                }
            }
        }
    }

    fun isPriceOnLine(currentPrice: Double): Boolean {
        if (averages.isNotEmpty()) {
            var m = 0.0
            var c = 0.0
            for (i in 0 until averages.size-1) {
                m = StraightLineFormulaUtils.calculateGradient((i+1).toDouble(), i.toDouble(), averages.elementAt(i+1), averages.elementAt(i))
                c = StraightLineFormulaUtils.calculateConstant((i+1).toDouble(), averages.elementAt(i+1), m)
                if (StraightLineFormulaUtils.isPointOnLine(i.toDouble(), currentPrice, m, c)) {
                    return true
                }
            }
        }
        return false
    }

    fun isPriceAboveLine(currentPrice: Double): Boolean {
        var toReturn: Boolean = false
        if (averages.isNotEmpty() && averages.size >= 20) {
            for (i in 0 until averages.size-1) {
                toReturn = currentPrice > averages.elementAt(i)
            }
        }
        return toReturn
    }
}