package za.co.botcoin.utils

import android.util.Log

object StraightLineFormulaUtils {
    fun calculateGradient(x2: Int, x1: Int, y2: Double, y1: Double): Double = MathUtils.precision((y2 - y1) / (x2 - x1))

    fun calculateConstant(x: Int, y: Double, m: Double): Double = MathUtils.precision(y - (m*x))

    private fun calculateX(y: Double, m: Double, c: Double): Double = MathUtils.precision((y-c) / m)

    private fun calculateY(x: Int, m: Double, c: Double): Double = MathUtils.precision((m*x) + c)

    fun isPointOnLine(x: Int, y: Double, m: Double, c: Double): Boolean {
        val calcY = calculateY(x, m, c)
        if (y == calcY) {
            return true
        }
        return false
    }
}