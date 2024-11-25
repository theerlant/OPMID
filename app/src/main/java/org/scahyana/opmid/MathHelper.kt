package org.scahyana.opmid

import androidx.compose.ui.geometry.Offset

class MathHelper {
    companion object {
        // Greatest Common Division find the highest number that can divide two numbers
        fun gcd(a: Int, b: Int): Int {
            return if (b == 0) a else gcd(b, a % b)
        }

        fun calculateOffset(basePosition: Offset, targetPosition: Offset): Offset {
            val offsetX = targetPosition.x - basePosition.x
            val offsetY = targetPosition.y - basePosition.y
            return Offset(offsetX, offsetY)
        }
    }
}