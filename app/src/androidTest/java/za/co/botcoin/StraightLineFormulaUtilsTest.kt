package za.co.botcoin

import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import za.co.botcoin.utils.StraightLineFormulaUtils.calculateGradient

class StraightLineFormulaUtilsTest {

    @Before
    fun setup() {

    }

    @Test
    fun should_calculate_gradientSuccess() {
        //given
        val pointA = Pair(3.0, 2.0)
        val pointB = Pair(1.0, 1.0)
        val expected = 0.5

        //when
        val actual = calculateGradient(pointA.second, pointB.second, pointA.first, pointB.first)

        //then
        assertEquals(expected, actual)
    }

    @Test
    fun should_calculate_gradientZero() {
        //given
        val pointA = Pair(3.0, 2.0)
        val pointB = Pair(1.0, 2.0)
        val expected = 0.0

        //when
        val actual = calculateGradient(pointA.second, pointB.second, pointA.first, pointB.first)

        //then
        assertEquals(expected, actual)
    }

    @Test
    fun should_calculate_gradientFailure() {
        //given
        val pointA = Pair(3.0, 2.0)
        val pointB = Pair(3.0, 1.0)
        val expected = 0.0

        //when
        val actual = calculateGradient(pointA.second, pointB.second, pointA.first, pointB.first)

        //then
        assertEquals(expected, actual)
    }
}