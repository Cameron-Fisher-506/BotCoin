package za.co.botcoin.utils

import za.co.botcoin.model.models.Candle
import za.co.botcoin.utils.StraightLineFormulaUtils.calculateConstant
import za.co.botcoin.utils.StraightLineFormulaUtils.calculateGradient
import za.co.botcoin.utils.StraightLineFormulaUtils.isPointOnLine
import java.util.*

class SimpleMovingAverage(var period: Int) {
    private val dataSet: Queue<Candle> = LinkedList()
    val averages: Queue<Double> = LinkedList()
    private var sum: Double = 0.0

    fun calculateSma(candles: List<Candle>) {
        if (dataSet.size >= period) {
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
            for (i in 1 until averages.size) {
                val m = calculateGradient(averages.elementAt(i), averages.elementAt(i-1), (i).toDouble(), (i-1).toDouble())
                val c = calculateConstant((i-1).toDouble(), averages.elementAt(i-1), m)
                if (isPointOnLine(i.toDouble() - 1, currentPrice, m, c)) {
                    return true
                }
            }
        }
        return false
    }
}