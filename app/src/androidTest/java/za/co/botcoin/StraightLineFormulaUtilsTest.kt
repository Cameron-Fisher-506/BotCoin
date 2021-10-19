package za.co.botcoin

import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import za.co.botcoin.utils.StraightLineFormulaUtils.calculateConstant
import za.co.botcoin.utils.StraightLineFormulaUtils.calculateGradient
import za.co.botcoin.utils.StraightLineFormulaUtils.calculateX
import za.co.botcoin.utils.StraightLineFormulaUtils.calculateY

class StraightLineFormulaUtilsTest {

    @Before
    fun setup() {

    }

    @Test
    fun shouldCalculateGradientSuccess() {
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
    fun shouldCalculateGradientZeroSuccess() {
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
    fun shouldCalculateGradientFailure() {
        //given
        val pointA = Pair(3.0, 2.0)
        val pointB = Pair(3.0, 1.0)
        val expected = 0.0

        //when
        val actual = calculateGradient(pointA.second, pointB.second, pointA.first, pointB.first)

        //then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldCalculateConstantSuccess() {
        //given
        val point = Pair(3.0, 2.0)
        val m = 0.5
        val expected = 0.5

        //when
        val actual = calculateConstant(point.first, point.second, m)

        //then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldCalculateXSuccess() {
        //given
        val y = 2.0
        val m = 0.5
        val c = 0.5
        val expected = 3.0

        //when
        val actual = calculateX(y, m, c)

        //then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldCalculateYSuccess() {
        //given
        val x = 3.0
        val m = 0.5
        val c = 0.5
        val expected = 2.0

        //when
        val actual = calculateY(x, m, c)

        //then
        assertEquals(expected, actual)
    }
}