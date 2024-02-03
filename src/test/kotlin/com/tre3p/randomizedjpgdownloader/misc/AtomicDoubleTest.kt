package com.tre3p.randomizedjpgdownloader.misc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AtomicDoubleTest {

    private lateinit var underTest: AtomicDouble

    @BeforeEach
    fun beforeEach() {
        underTest = AtomicDouble()
    }

    /**
     * Checks if single-threaded addition is correct
     */
    @Test
    fun shouldCorrectlyAddDoubleValue() {
        val initVal = 123.456
        val toAddVal = 456.789

        underTest.addAndGet(initVal)
        assertEquals(123.456, underTest.get())
        val additionResult = underTest.addAndGet(456.789)
        assertEquals(initVal + toAddVal, additionResult)
    }

}