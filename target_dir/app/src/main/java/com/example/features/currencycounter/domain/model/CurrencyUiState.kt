package com.example.features.currencycounter.domain.model

data class CurrencyUiState(
    val counts: Map<Int, Int> = mapOf(
        500 to 0,
        200 to 0,
        100 to 0,
        50 to 0,
        20 to 0,
        10 to 0
    ),
    val totalAmount: Double = 0.0,
    val totalNotes: Int = 0
)
