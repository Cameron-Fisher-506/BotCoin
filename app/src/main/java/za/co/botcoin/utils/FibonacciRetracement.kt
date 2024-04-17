package za.co.botcoin.utils

import za.co.botcoin.state.Trend
import za.co.botcoin.model.models.Candle

class FibonacciRetracement {
    val retracements: ArrayList<Double> = arrayListOf()
    private val percentages: List<Double> = listOf(0.236, 0.382, 0.5, 0.618, 0.764)

    fun calculateRetracements(highestCandle: Candle, lowestCandle: Candle, marketTrend: Trend) {
        if (marketTrend == Trend.UPWARD) {
            retracements.add(lowestCandle.low.toDouble())
        } else {
            retracements.add(highestCandle.high.toDouble())
        }

        val difference = highestCandle.high.toDouble() - lowestCandle.low.toDouble()
        percentages.map {
            val result = difference * it
            if (marketTrend == Trend.UPWARD) {
                retracements.add(MathUtils.precision(lowestCandle.low.toDouble() + result))
            } else {
                retracements.add(MathUtils.precision(highestCandle.high.toDouble() - result))
            }
        }

        if (marketTrend == Trend.UPWARD) {
            retracements.add(highestCandle.high.toDouble())
        } else {

            retracements.add(lowestCandle.low.toDouble())
        }
    }
}