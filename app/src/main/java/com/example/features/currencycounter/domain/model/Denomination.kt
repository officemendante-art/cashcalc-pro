package com.example.features.currencycounter.domain.model

data class Denomination(
    val value: Int,
    val label: String,
    val hexColor: Long // Color hue accent matching real Indian currency notes
) {
    companion object {
        val IndianRupeeBills = listOf(
            Denomination(500, "₹500", 0xFF697A6D), // Stone Grey note
            Denomination(200, "₹200", 0xFFD87C39), // Bright Yellow-Orange note
            Denomination(100, "₹100", 0xFF837F9C), // Lavender note
            Denomination(50,  "₹50",  0xFF3C9EBE), // Fluorescent Blue note
            Denomination(20,  "₹20",  0xFF898A44), // Greenish Yellow note
            Denomination(10,  "₹10",  0xFF825D4F)  // Chocolate Brown note
        )
    }
}
