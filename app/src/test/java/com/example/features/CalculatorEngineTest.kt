package com.example.features

import com.example.features.calculator.domain.CalculatorEngine
import com.example.features.currencycounter.domain.CurrencyFormatter
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorEngineTest {

    private val engine = CalculatorEngine()
    private val formatter = CurrencyFormatter()

    @Test
    fun testBasicMathOperations() {
        assertEquals("10", engine.evaluate("6+4"))
        assertEquals("5.5", engine.evaluate("10-4.5"))
        assertEquals("12", engine.evaluate("4*3"))
        assertEquals("2.5", engine.evaluate("5/2"))
    }

    @Test
    fun testOperatorPrecedence() {
        assertEquals("14", engine.evaluate("2+3*4"))
        assertEquals("10.5", engine.evaluate("12.5-4/2"))
    }

    @Test
    fun testDivideByZeroHandling() {
        assertEquals("Error: Div by 0", engine.evaluate("5/0"))
    }

    @Test
    fun testIndianRupeeGroupingFormat() {
        assertEquals("₹500", formatter.formatRupee(500.0))
        assertEquals("₹1,500", formatter.formatRupee(1500.0))
        assertEquals("₹1,50,000", formatter.formatRupee(150000.0))
        assertEquals("₹10,50,00,000", formatter.formatRupee(105000000.0))
    }

    @Test
    fun testRequestedQualityStandardTestCases() {
        // 0.1 + 0.2 = 0.3
        assertEquals("0.3", engine.evaluate("0.1 + 0.2"))

        // 1 / 3 = rounded cleanly
        assertEquals("0.333333333333", engine.evaluate("1 / 3"))

        // 2 + 3 × 4 = 14
        assertEquals("14", engine.evaluate("2 + 3 × 4"))

        // 10 ÷ 0 = Error
        assertEquals("Error: Div by 0", engine.evaluate("10 ÷ 0"))

        // 50% = 0.5
        assertEquals("0.5", engine.evaluate("50%"))

        // 100 + 10% = 110
        assertEquals("110", engine.evaluate("100 + 10%"))

        // 999999999 + 1 = 1000000000
        assertEquals("1000000000", engine.evaluate("999999999 + 1"))
    }

    @Test
    fun testTrailingAndRepeatedOperators() {
        // trailing operator gets safely dropped
        assertEquals("100", engine.evaluate("100+"))
        assertEquals("102", engine.evaluate("100 + 2 -"))

        // repeated operators get normalized
        assertEquals("102", engine.evaluate("100 ++ 2"))
        assertEquals("98", engine.evaluate("100 +- 2"))
        assertEquals("999200000", engine.evaluate("999999999 + 1 - 800000 *"))
    }
}
