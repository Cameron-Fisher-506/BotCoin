package za.co.botcoin.utils

import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import za.co.botcoin.utils.MathUtils.calculateMarginPercentage
import za.co.botcoin.utils.MathUtils.percentage
import za.co.botcoin.utils.MathUtils.precision
import za.co.botcoin.utils.MathUtils.reverse

@DisplayName("Math Utils Test")
class MathUtilsTest {

    @BeforeEach
    fun setup() {

    }

    @Test
    @DisplayName("Should Calculate Pi With Precision")
    fun shouldCalculatePiWithPrecision() {
        //Given
        val pi = Math.PI
        val expected: Double = 3.14

        //When
        val actual = precision(pi)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Should Calculate Percentage")
    fun shouldCalculatePercentage() {
        //Given
        val expected = 10.000000149011612

        //When
        val actual = percentage(100.0, 10)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Should Calculate percentage With Precision")
    fun shouldCalculatePercentageWithPrecision() {
        //given
        val expected = 10.00

        //when
        val actual = precision(percentage(100.0, 10))

        //then
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Should Calculate Higher Margin Percentage")
    fun shouldCalculateHigherMarginPercentage() {
        //given
        val expected = 126.0

        //when
        val actual = calculateMarginPercentage(10.0, 12.0, 5, false)

        //then
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Should Calculate Lower Margin Percentage")
    fun shouldCalculateLowerMarginPercentage() {
        //given
        val expected = 114.0

        //when
        val actual = calculateMarginPercentage(10.0, 12.0, 5)

        //then
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Should Calculate Higher Margin Percentage Of Price")
    fun shouldCalculateHigherMarginPercentageOfPrice() {
        //given
        val expected = 157.5

        //when
        val actual = calculateMarginPercentage(150.0, 5, false)

        //then
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Should Calculate Lower margin Percentage Of Price")
    fun shouldCalculateLowerMarginPercentageOfPrice() {
        //given
        val expected = 142.5

        //when
        val actual = calculateMarginPercentage(150.0, 5)

        //then
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Should Calculate Reverse Value")
    fun shouldCalculateReverseValue() {
        //given
        val expected = -5.0

        //when
        val actual = reverse(5.0)

        //then
        assertEquals(expected, actual)
    }
}