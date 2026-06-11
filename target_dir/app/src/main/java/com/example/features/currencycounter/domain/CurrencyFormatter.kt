package com.example.features.currencycounter.domain

import java.util.Locale

class CurrencyFormatter {

    fun formatRupee(amount: Double): String {
        val wholePart = amount.toLong()
        val formattedInt = applyIndianGrouping(wholePart)
        
        // Return without decimals if whole, but if there's any cents / paisa, format with decimals.
        val decimalPart = amount - wholePart
        return if (decimalPart > 0.005) {
            val decStr = String.format(Locale.US, "%.2f", decimalPart).substring(1) // gets ".XX"
            "₹$formattedInt$decStr"
        } else {
            "₹$formattedInt"
        }
    }

    private fun applyIndianGrouping(value: Long): String {
        val numStr = value.toString()
        if (numStr.length <= 3) return numStr

        val lastThree = numStr.substring(numStr.length - 3)
        var head = numStr.substring(0, numStr.length - 3)

        val sb = StringBuilder()
        while (head.length > 2) {
            val chunk = head.substring(head.length - 2)
            sb.insert(0, ",$chunk")
            head = head.substring(0, head.length - 2)
        }
        if (head.isNotEmpty()) {
            sb.insert(0, head)
        }
        sb.append(",$lastThree")
        return sb.toString()
    }
}
