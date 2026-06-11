package com.example.features.calculator.domain

import com.ezylang.evalex.Expression
import java.math.BigDecimal
import java.math.RoundingMode

class CalculatorEngine {

    fun evaluate(expression: String): String {
        val trimmed = expression.trim()
        if (trimmed.isEmpty() || trimmed == "0") return "0"
        
        return try {
            val normalized = trimmed
                .replace("×", "*")
                .replace("÷", "/")
                .replace(" ", "")
            
            // 1. Normalize repeated/consecutive operators first
            val cleanedOps = normalizeOperators(normalized)
            
            // 2. Safely strip trailing operators
            val readyForPercent = stripTrailingOperators(cleanedOps)
            if (readyForPercent.isEmpty()) return "0"
            
            // 3. Robustly convert percentage symbols
            val fullyParsedString = convertPercentages(readyForPercent)
            
            // 4. Evaluate using EvalEx
            val expr = Expression(fullyParsedString)
            val resultValue = expr.evaluate()
            
            // Extract BigDecimal result cleanly
            val bigDecimalResult = resultValue.getNumberValue()
                ?: BigDecimal(resultValue.getStringValue() ?: "0")
            
            formatResult(bigDecimalResult)
        } catch (e: java.lang.ArithmeticException) {
            "Error: Div by 0"
        } catch (e: Exception) {
            val message = e.message ?: ""
            if (message.contains("Division by zero", ignoreCase = true) || message.contains("divide by zero", ignoreCase = true)) {
                "Error: Div by 0"
            } else {
                "Error"
            }
        }
    }

    private fun normalizeOperators(expr: String): String {
        var res = expr
        var prev = ""
        while (res != prev) {
            prev = res
            res = res
                .replace("++", "+")
                .replace("+-", "-")
                .replace("+*", "*")
                .replace("+/", "/")
                .replace("-+", "+")
                .replace("--", "+")
                .replace("-*", "*")
                .replace("-/", "/")
                .replace("*+", "*")
                .replace("**", "*")
                .replace("*/", "/")
                .replace("/+", "/")
                .replace("/*", "*")
                .replace("//", "/")
        }
        return res
    }

    private fun stripTrailingOperators(expr: String): String {
        var cleaned = expr
        while (cleaned.isNotEmpty() && (cleaned.endsWith("+") || cleaned.endsWith("-") || cleaned.endsWith("*") || cleaned.endsWith("/"))) {
            cleaned = cleaned.dropLast(1)
        }
        return cleaned
    }

    /**
     * Converts standard calculator percentage behaviour.
     * Searches for Y% and processes it to (Y/100) or (X * Y / 100) based on contextual operator.
     */
    private fun convertPercentages(expr: String): String {
        var current = expr
        while (current.contains("%")) {
            val percentIndex = current.indexOf("%")
            if (percentIndex == 0) {
                current = "0.01" + current.substring(1)
                continue
            }

            // Find start of the number before percent
            var numStart = percentIndex - 1
            while (numStart >= 0 && (current[numStart].isDigit() || current[numStart] == '.')) {
                numStart--
            }
            numStart++ // Correct offset

            if (numStart >= percentIndex) {
                current = current.removeRange(percentIndex, percentIndex + 1)
                continue
            }

            val percentNumStr = current.substring(numStart, percentIndex)
            val percentVal = percentNumStr.toDoubleOrNull() ?: 0.0

            // Check connection operator before this number
            val opIndex = numStart - 1
            if (opIndex >= 0) {
                val connectingOp = current[opIndex]
                val precedingExpr = current.substring(0, opIndex)

                if ((connectingOp == '+' || connectingOp == '-') && precedingExpr.trim().isNotEmpty()) {
                    try {
                        val parsedPreceding = convertPercentages(precedingExpr)
                        val baseEval = Expression(parsedPreceding).evaluate()
                        val baseValue = baseEval.getNumberValue() ?: BigDecimal.ZERO
                        
                        val replacement = "($baseValue * $percentVal / 100)"
                        current = current.substring(0, numStart) + replacement + current.substring(percentIndex + 1)
                    } catch (e: Exception) {
                        val replacement = "($percentVal / 100)"
                        current = current.substring(0, numStart) + replacement + current.substring(percentIndex + 1)
                    }
                } else {
                    val replacement = "($percentVal / 100)"
                    current = current.substring(0, numStart) + replacement + current.substring(percentIndex + 1)
                }
            } else {
                val replacement = "($percentVal / 100)"
                current = current.substring(0, numStart) + replacement + current.substring(percentIndex + 1)
            }
        }
        return current
    }

    private fun formatResult(value: BigDecimal): String {
        val bd = value.setScale(12, RoundingMode.HALF_UP).stripTrailingZeros()
        val plain = bd.toPlainString()
        
        return if (plain.contains(".") && plain.endsWith(".0")) {
            plain.substring(0, plain.length - 2)
        } else {
            plain
        }
    }
}
