package com.tre3p.randomizedjpgdownloader.misc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.round

class AtomicDoubleConcurrencyTest {

    private val threadPoolSize = Runtime.getRuntime().availableProcessors() * 2

    private lateinit var executorService: ExecutorService

    private val toAddDoubleValues = listOf(123.456, 12.8533, 75.1, 99.9999, 0.892, 10.999, 123.444, 122.12, 0.12)

    private lateinit var underTest: AtomicDouble

    @BeforeEach
    fun beforeEach() {
        underTest = AtomicDouble()
        executorService = Executors.newFixedThreadPool(threadPoolSize)
    }

    /**
     * Checks if multithreaded addition is correct.
     * Correctness of addition is checked with values rounded to 4 decimal places.
     */
    @RepeatedTest(10)
    fun shouldCorrectlyAddDoubleValuesWhenAccessedConcurrently() {
        val cdl = CountDownLatch(1)

        for (i in 1..threadPoolSize) {
            executorService.submit {
                try {
                    cdl.await()
                } catch (e: InterruptedException) { }

                for (doubleValue in toAddDoubleValues.shuffled()) {
                    underTest.addAndGet(doubleValue)
                }
            }
        }

        cdl.countDown()
        executorService.shutdown()
        executorService.awaitTermination(5, TimeUnit.SECONDS)

        val expectedValue = (toAddDoubleValues.sum() * threadPoolSize).round(4)
        val actualValue = underTest.get().round(4)

        assertEquals(expectedValue, actualValue)
    }

    /**
     * Rounds double to N decimal places
     */
    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
}