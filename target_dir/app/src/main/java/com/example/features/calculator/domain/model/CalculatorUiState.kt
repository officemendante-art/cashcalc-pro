package com.example.features.calculator.domain.model

data class CalculatorUiState(
    val equation: String = "",
    val displayValue: String = "0",
    val historyExpression: String = "",
    val isOutputResult: Boolean = false,
    val hasError: Boolean = false
)
