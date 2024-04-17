package za.co.botcoin.utils

import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import za.co.botcoin.state.Trend
import za.co.botcoin.model.models.Candle

@DisplayName("Fibonacci Retracement Test")
class FibonacciRetracementTest {

    private val fibonacciRetracement: FibonacciRetracement = FibonacciRetracement()

    @Before
    fun setup() {

    }

    @Test
    @DisplayName("Should Calculate Fibonacci Retracement When Market Is In Downward Trend")
    fun shouldCalculateFibonacciRetracementWhenMarketIsInDownwardTrend() {
        //given
        val highestCandle = Candle().apply {
            high = "30"
        }

        val lowestCandle = Candle().apply {
            low = "10"
        }

        val marketTrend = Trend.DOWNWARD

        val expected = listOf(30.0, 25.28, 22.36, 20.0, 17.64, 14.72, 10.0)

        //when
        fibonacciRetracement.calculateRetracements(highestCandle, lowestCandle, marketTrend)
        val actual = fibonacciRetracement.retracements

        //then
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Should Calculate Fibonacci Retracement When Market Is In Upward Trend")
    fun shouldCalculateFibonacciRetracementWhenMarketIsInUpwardTrend() {
        //given
        val highestCandle = Candle().apply {
            high = "30"
        }

        val lowestCandle = Candle().apply {
            low = "10"
        }

        val marketTrend = Trend.UPWARD

        val expected = listOf(10.0, 14.72, 17.64, 20.0, 22.36, 25.28, 30.0)

        //when
        fibonacciRetracement.calculateRetracements(highestCandle, lowestCandle, marketTrend)
        val actual = fibonacciRetracement.retracements

        //then
        assertEquals(expected, actual)
    }
}