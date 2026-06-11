package com.example.features.calculator.domain

object NumberToWordsConverter {

    private val units = arrayOf(
        "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
        "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
    )

    private val tens = arrayOf(
        "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    )

    fun convertExpression(expression: String): String {
        // Strip out commas first from the expression if they exist
        val cleanExpression = expression.replace(",", "")
        
        val numberRegex = Regex("\\d+(?:\\.\\d+)?")
        return numberRegex.replace(cleanExpression) { matchResult ->
            val numStr = matchResult.value
            convertNumber(numStr)
        }.replace("×", " × ")
         .replace("÷", " ÷ ")
         .replace("+", " + ")
         .replace("-", " - ")
         .replace(Regex(" +"), " ") // Cleanup double spaces
         .trim()
         .lowercase()
    }

    private fun convertNumber(numberStr: String): String {
        return try {
            if (numberStr == "0") return "zero"
            
            val isNegative = numberStr.startsWith("-")
            val absStr = numberStr.removePrefix("-")
            
            val parts = absStr.split(".")
            val integerPart = parts[0]
            val fractionalPart = if (parts.size > 1) parts[1] else ""
            
            if (integerPart.length > 15) return "number too large" // Limits to 100 Lakh Crores

            val intValue = integerPart.toLong()
            var words = convertWholeNumber(intValue)
            
            if (fractionalPart.isNotEmpty() && fractionalPart.any { it != '0' }) {
                 val fracWords = fractionalPart.map { char ->
                     if (char == '0') "Zero" else units[char.toString().toInt()]
                 }.joinToString(" ")
                 words += " point $fracWords"
            }
            
            val result = if (words.isBlank()) "zero" else words.trim()
            if (isNegative) "minus $result" else result
        } catch (e: Exception) {
            ""
        }
    }

    private fun convertWholeNumber(num: Long): String {
        if (num == 0L) return ""
        if (num < 20) return units[num.toInt()]
        if (num < 100) return tens[(num / 10).toInt()] + if (num % 10 != 0L) " " + units[(num % 10).toInt()] else ""
        if (num < 1000) return units[(num / 100).toInt()] + " Hundred" + if (num % 100 != 0L) " and " + convertWholeNumber(num % 100) else ""
        if (num < 100000) return convertWholeNumber(num / 1000) + " Thousand" + if (num % 1000 != 0L) " " + convertWholeNumber(num % 1000) else ""
        if (num < 10000000) return convertWholeNumber(num / 100000) + " Lakh" + if (num % 100000 != 0L) " " + convertWholeNumber(num % 100000) else ""
        return convertWholeNumber(num / 10000000) + " Crore" + if (num % 10000000 != 0L) " " + convertWholeNumber(num % 10000000) else ""
    }
}
