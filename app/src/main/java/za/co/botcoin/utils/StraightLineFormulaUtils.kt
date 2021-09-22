package za.co.botcoin.utils

import android.util.Log
import za.co.botcoin.utils.MathUtils.reverse
import kotlin.math.abs
import kotlin.math.sqrt

object  StraightLineFormulaUtils {
    fun calculateGradient(x2: Double, x1: Double, y2: Double, y1: Double): Double = (y2 - y1) / (x2 - x1)

    fun calculateConstant(x: Double, y: Double, m: Double): Double = y - (m*x)

    fun calculateX(y: Double, m: Double, c: Double): Double = MathUtils.precision((y-c) / m)

    fun calculateY(x: Double, m: Double, c: Double): Double = MathUtils.precision((m*x) + c)

    fun isPointOnLine(x: Double, y: Double, m: Double, c: Double): Boolean {
        //y = mx + c
        //mx -y + c = 0

        val d = MathUtils.precision(abs(m*x + reverse(y) + c) / sqrt(m*m + 1*1))
        Log.d("BOTCOIN", "d: $d")
        if (d == 0.0) {
            return true
        }
        return false
    }
}