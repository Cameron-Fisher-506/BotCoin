package za.co.botcoin

import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import za.co.botcoin.utils.MathUtils

class MathUtilsTest {

    @Before
    fun setup() {

    }

    @Test
    fun should_CalculatePi_With_Precision() {
        //Given
        val pi = Math.PI
        val expected: Double = 3.14

        //When
        val actual = MathUtils.precision(pi)

        //Then
        assertEquals(expected, actual)
    }
}