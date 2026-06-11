package com.example.features.calculator.domain.model

sealed interface CalculatorAction {
    data class Number(val value: String) : CalculatorAction
    data class Operator(val op: String) : CalculatorAction
    object Decimal : CalculatorAction
    object Evaluate : CalculatorAction
    object Clear : CalculatorAction
    object Backspace : CalculatorAction
    object Percent : CalculatorAction
    object ClearHistory : CalculatorAction
}
