package za.co.botcoin.utils

import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import za.co.botcoin.utils.StraightLineFormulaUtils.calculateConstant
import za.co.botcoin.utils.StraightLineFormulaUtils.calculateGradient
import za.co.botcoin.utils.StraightLineFormulaUtils.calculateX
import za.co.botcoin.utils.StraightLineFormulaUtils.calculateY
import za.co.botcoin.utils.StraightLineFormulaUtils.isPointOnLine

@DisplayName("Straight Line Formula Utils Test")
class StraightLineFormulaUtilsTest {

    @Before
    fun setup() {

    }

    @Test
    @DisplayName("Should Calculate Gradient Success")
    fun shouldCalculateGradientSuccess() {
        //given
        val pointA = Pair(1.0, 1.0)
        val pointB = Pair(2.0, 3.0)
        val expected = 2.0

        //when
        val actual = calculateGradient(pointA.second, pointB.second, pointA.first, pointB.first)

        //then
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Should Calculate Gradient Zero Success")
    fun shouldCalculateGradientZeroSuccess() {
        //given
        val pointA = Pair(1.0, 2.0)
        val pointB = Pair(2.0, 2.0)
        val expected = 0.0

        //when
        val actual = calculateGradient(pointB.second, pointA.second, pointB.first, pointA.first)

        //then
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Should Calculate Gradient Failure")
    fun shouldCalculateGradientFailure() {
        //given
        val pointA = Pair(3.0, 2.0)
        val pointB = Pair(3.0, 1.0)
        val expected = 0.0

        //when
        val actual = calculateGradient(pointB.second, pointA.second, pointB.first, pointA.first)

        //then
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Should Calculate Constant Success")
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
    @DisplayName("Should Calculate X Success")
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
    @DisplayName("Should Calculate Y Success")
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

    @Test
    @DisplayName("Should Calculate Is Point On Line Success")
    fun shouldCalculateIsPointOnLineSuccess() {
        //given
        val pointOne = Pair(1.0, 1.0)
        val pointTwo = Pair(2.0, 1.5)
        val pointThree = Pair(3.0, 2.0)
        val pointFour = Pair(4.0, 2.5)
        val m = 0.5
        val c = 0.5
        val expected = true

        //when
        val actualPointOne = isPointOnLine(pointOne.first, pointOne.second, m, c)
        val actualPointTwo = isPointOnLine(pointTwo.first, pointTwo.second, m, c)
        val actualPointThree = isPointOnLine(pointThree.first, pointThree.second, m, c)
        val actualPointFour = isPointOnLine(pointFour.first, pointFour.second, m, c)

        //then
        assertEquals(expected, actualPointOne)
        assertEquals(expected, actualPointTwo)
        assertEquals(expected, actualPointThree)
        assertEquals(expected, actualPointFour)
    }

    @Test
    @DisplayName("Should Calculate Is Point On Line Failure")
    fun shouldCalculateIsPointOnLineFailure() {
        //given
        val pointOne = Pair(1.0, 1.2)
        val pointTwo = Pair(2.0, 1.7)
        val pointThree = Pair(3.0, 2.2)
        val pointFour = Pair(4.0, 2.7)
        val m = 0.5
        val c = 0.5
        val expected = false

        //when
        val actualPointOne = isPointOnLine(pointOne.first, pointOne.second, m, c)
        val actualPointTwo = isPointOnLine(pointTwo.first, pointTwo.second, m, c)
        val actualPointThree = isPointOnLine(pointThree.first, pointThree.second, m, c)
        val actualPointFour = isPointOnLine(pointFour.first, pointFour.second, m, c)

        //then
        assertEquals(expected, actualPointOne)
        assertEquals(expected, actualPointTwo)
        assertEquals(expected, actualPointThree)
        assertEquals(expected, actualPointFour)
    }
}