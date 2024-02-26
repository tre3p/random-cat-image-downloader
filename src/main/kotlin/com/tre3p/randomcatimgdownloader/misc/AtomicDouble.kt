package com.tre3p.randomcatimgdownloader.misc

import java.util.concurrent.atomic.AtomicLong

/**
 * Implementation of AtomicDouble class based on storing IEEE 754 floating-point bit layout in AtomicLong variable
 *
 * More on this: https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/package-summary.html
 *               https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#doubleToRawLongBits-double-
 *               https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#longBitsToDouble-long-
 */
class AtomicDouble(initValue: Double = 0.0) {

    private val value: AtomicLong = AtomicLong(initValue.toRawBits())

    fun addAndGet(toAddValue: Double): Double {
        return Double.fromBits(this.value.addDoubleBits(toAddValue))
    }

    fun get(): Double {
        return Double.fromBits(this.value.get())
    }

    private fun AtomicLong.addDoubleBits(double: Double) = this.updateAndGet { prevVal -> (Double.fromBits(prevVal) + double).toRawBits() }
}