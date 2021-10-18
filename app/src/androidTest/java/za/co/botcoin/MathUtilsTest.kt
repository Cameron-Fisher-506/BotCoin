package za.co.botcoin

import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import za.co.botcoin.utils.MathUtils.calcMarginPercentage
import za.co.botcoin.utils.MathUtils.percentage
import za.co.botcoin.utils.MathUtils.precision
import za.co.botcoin.utils.MathUtils.reverse

class MathUtilsTest {

    @Before
    fun setup() {

    }

    @Test
    fun should_calculatePi_with_precision() {
        //Given
        val pi = Math.PI
        val expected: Double = 3.14

        //When
        val actual = precision(pi)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun should_calculate_percentage() {
        //Given
        val expected = 10.000000149011612

        //When
        val actual = percentage(100.0, 10)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun should_calculate_percentageWithPrecision() {
        //given
        val expected = 10.00

        //when
        val actual = precision(percentage(100.0, 10))

        //then
        assertEquals(expected, actual)
    }

    @Test
    fun should_calculate_higherMarginPercentage() {
        //given
        val expected = 126.0

        //when
        val actual = calcMarginPercentage(10.0, 12.0, 5, false)

        //then
        assertEquals(expected, actual)
    }

    @Test
    fun should_calculate_lowerMarginPercentage() {
        //given
        val expected = 114.0

        //when
        val actual = calcMarginPercentage(10.0, 12.0, 5)

        //then
        assertEquals(expected, actual)
    }

    @Test
    fun should_calculate_higherMarginPercentageOfPrice() {
        //given
        val expected = 157.5

        //when
        val actual = calcMarginPercentage(150.0, 5, false)

        //then
        assertEquals(expected, actual)
    }

    @Test
    fun should_calculate_lowerMarginPercentageOfPrice() {
        //given
        val expected = 142.5

        //when
        val actual = calcMarginPercentage(150.0, 5)

        //then
        assertEquals(expected, actual)
    }

    @Test
    fun should_calculate_reverseValue() {
        //given
        val expected = -5.0

        //when
        val actual = reverse(5.0)

        //then
        assertEquals(expected, actual)
    }
}