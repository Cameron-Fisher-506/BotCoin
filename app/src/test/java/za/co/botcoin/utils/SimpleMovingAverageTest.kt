package za.co.botcoin.utils

import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import za.co.botcoin.model.models.Candle

@DisplayName("Simple Moving Average Test")
class SimpleMovingAverageTest {
    private lateinit var simpleMovingAverage: SimpleMovingAverage

    @Before
    fun setup() {
        this.simpleMovingAverage = SimpleMovingAverage(5)
    }

    private fun getNewCandle(close: String, timestamp: String): Candle = Candle().apply {
        this.close = close
        this.timestamp = timestamp
    }

    @Test
    @DisplayName("Should Calculate SMA Success")
    fun shouldCalculateSmaSuccess() {
        //given
        val candles: List<Candle> = ArrayList<Candle>().apply {
            add(getNewCandle("3.0", "19-10-2021 10:00:00"))
            add(getNewCandle("3.34", "19-10-2021 11:00:00"))
            add(getNewCandle("4.0", "19-10-2021 12:00:00"))
            add(getNewCandle("4.1", "19-10-2021 13:00:00"))
            add(getNewCandle("3.85", "19-10-2021 14:00:00"))
        }
        val candlesTwo: List<Candle> = ArrayList<Candle>().apply {
            add(getNewCandle("3.0", "19-10-2021 10:00:00"))
            add(getNewCandle("3.34", "19-10-2021 11:00:00"))
            add(getNewCandle("4.0", "19-10-2021 12:00:00"))
            add(getNewCandle("4.1", "19-10-2021 13:00:00"))
            add(getNewCandle("3.85", "19-10-2021 14:00:00"))
            add(getNewCandle("3.9", "19-10-2021 15:00:00"))
        }
        val candlesThree: List<Candle> = ArrayList<Candle>().apply {
            add(getNewCandle("3.0", "19-10-2021 10:00:00"))
            add(getNewCandle("3.34", "19-10-2021 11:00:00"))
            add(getNewCandle("4.0", "19-10-2021 12:00:00"))
            add(getNewCandle("4.1", "19-10-2021 13:00:00"))
            add(getNewCandle("3.85", "19-10-2021 14:00:00"))
            add(getNewCandle("3.9", "19-10-2021 15:00:00"))
            add(getNewCandle("4.3", "19-10-2021 16:00:00"))
        }
        val candlesFour: List<Candle> = ArrayList<Candle>().apply {
            add(getNewCandle("3.0", "19-10-2021 10:00:00"))
            add(getNewCandle("3.34", "19-10-2021 11:00:00"))
            add(getNewCandle("4.0", "19-10-2021 12:00:00"))
            add(getNewCandle("4.1", "19-10-2021 13:00:00"))
            add(getNewCandle("3.85", "19-10-2021 14:00:00"))
            add(getNewCandle("3.9", "19-10-2021 15:00:00"))
            add(getNewCandle("4.3", "19-10-2021 16:00:00"))
            add(getNewCandle("4.5", "19-10-2021 17:00:00"))
        }
        val candlesFive: List<Candle> = ArrayList<Candle>().apply {
            add(getNewCandle("3.0", "19-10-2021 10:00:00"))
            add(getNewCandle("3.34", "19-10-2021 11:00:00"))
            add(getNewCandle("4.0", "19-10-2021 12:00:00"))
            add(getNewCandle("4.1", "19-10-2021 13:00:00"))
            add(getNewCandle("3.85", "19-10-2021 14:00:00"))
            add(getNewCandle("3.9", "19-10-2021 15:00:00"))
            add(getNewCandle("4.3", "19-10-2021 16:00:00"))
            add(getNewCandle("4.5", "19-10-2021 17:00:00"))
            add(getNewCandle("4.2", "19-10-2021 18:00:00"))
        }
        val candlesSix: List<Candle> = ArrayList<Candle>().apply {
            add(getNewCandle("3.0", "19-10-2021 10:00:00"))
            add(getNewCandle("3.34", "19-10-2021 11:00:00"))
            add(getNewCandle("4.0", "19-10-2021 12:00:00"))
            add(getNewCandle("4.1", "19-10-2021 13:00:00"))
            add(getNewCandle("3.85", "19-10-2021 14:00:00"))
            add(getNewCandle("3.9", "19-10-2021 15:00:00"))
            add(getNewCandle("4.3", "19-10-2021 16:00:00"))
            add(getNewCandle("4.5", "19-10-2021 17:00:00"))
            add(getNewCandle("4.2", "19-10-2021 18:00:00"))
            add(getNewCandle("3.7", "19-10-2021 19:00:00"))
        }

        val expected = listOf<Double>(3.84, 4.03, 4.13, 4.15, 4.12)

        //when
        this.simpleMovingAverage.calculateSma(candles)
        this.simpleMovingAverage.calculateSma(candlesTwo)
        this.simpleMovingAverage.calculateSma(candlesThree)
        this.simpleMovingAverage.calculateSma(candlesFour)
        this.simpleMovingAverage.calculateSma(candlesFive)
        this.simpleMovingAverage.calculateSma(candlesSix)

        //then
        assertEquals(expected, this.simpleMovingAverage.averages)
    }

    @Test
    @DisplayName("Should Calculate Is Price On Line Success")
    fun shouldCalculateIsPriceOnLineSuccess() {
        //given
        shouldCalculateSmaSuccess()
        val currentPrice: String = "3.99"
        val expected: Boolean = true

        //when
        val actual = this.simpleMovingAverage.isPriceOnLine(currentPrice.toDouble())

        //then
        assertEquals(expected, actual)
    }
}