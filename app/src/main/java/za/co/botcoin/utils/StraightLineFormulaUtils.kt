package za.co.botcoin.utils

import za.co.botcoin.utils.MathUtils.precisionOneDecimal
import za.co.botcoin.utils.MathUtils.reverse
import kotlin.math.abs
import kotlin.math.sqrt

object StraightLineFormulaUtils {
    fun calculateGradient(y2: Double, y1: Double, x2: Double, x1: Double): Double = if ((x2 - x1) != 0.0) (y2 - y1) / (x2 - x1) else 0.0

    fun calculateConstant(x: Double, y: Double, m: Double): Double = y - (m * x)

    fun calculateX(y: Double, m: Double, c: Double): Double = MathUtils.precision((y - c) / m)

    fun calculateY(x: Double, m: Double, c: Double): Double = MathUtils.precision((m * x) + c)

    fun isPointOnLine(x: Double, y: Double, m: Double, c: Double): Boolean = precisionOneDecimal(abs(m * x + reverse(y) + c) / sqrt(m * m + 1 * 1)) == 0.0
}