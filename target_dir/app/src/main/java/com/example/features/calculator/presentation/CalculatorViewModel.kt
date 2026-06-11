package com.example.features.calculator.presentation

import androidx.lifecycle.ViewModel
import com.example.features.calculator.domain.CalculatorEngine
import com.example.features.calculator.domain.model.CalculatorAction
import com.example.features.calculator.domain.model.CalculatorUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalculatorViewModel : ViewModel() {

    private val engine = CalculatorEngine()
    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> handleNumber(action.value)
            is CalculatorAction.Operator -> handleOperator(action.op)
            is CalculatorAction.Decimal -> handleDecimal()
            is CalculatorAction.Backspace -> handleBackspace()
            is CalculatorAction.Percent -> handlePercent()
            is CalculatorAction.Clear -> handleClear()
            is CalculatorAction.Evaluate -> handleEvaluate()
        }
    }

    private fun handleNumber(num: String) {
        _uiState.update { state ->
            if (state.hasError) {
                CalculatorUiState(equation = num, displayValue = num)
            } else if (state.isOutputResult) {
                state.copy(equation = num, displayValue = num, isOutputResult = false)
            } else {
                val newEquation = if (state.equation == "0") num else state.equation + num
                state.copy(equation = newEquation, displayValue = newEquation)
            }
        }
    }

    private fun handleOperator(op: String) {
        _uiState.update { state ->
            if (state.hasError) {
                state
            } else {
                val currentEq = if (state.equation.isEmpty()) "0" else state.equation
                val lastChar = currentEq.lastOrNull()
                val newEquation = if (lastChar != null && lastChar in listOf('+', '-', '×', '÷')) {
                    currentEq.dropLast(1) + op
                } else {
                    currentEq + op
                }
                state.copy(equation = newEquation, displayValue = newEquation, isOutputResult = false)
            }
        }
    }

    private fun handleDecimal() {
        _uiState.update { state ->
            if (state.hasError) return@update state
            val currentEq = if (state.equation.isEmpty() || state.isOutputResult) "0" else state.equation
            val tokens = currentEq.split('+', '-', '×', '÷')
            val currentToken = tokens.lastOrNull() ?: ""
            
            if (!currentToken.contains('.')) {
                val newEq = currentEq + "."
                state.copy(equation = newEq, displayValue = newEq, isOutputResult = false)
            } else {
                state
            }
        }
    }

    private fun handleBackspace() {
        _uiState.update { state ->
            if (state.hasError || state.isOutputResult) {
                CalculatorUiState()
            } else {
                val newEq = if (state.equation.length <= 1) "0" else state.equation.dropLast(1)
                state.copy(equation = newEq, displayValue = newEq)
            }
        }
    }

    private fun handlePercent() {
        _uiState.update { state ->
            if (state.hasError || state.equation.isEmpty()) return@update state
            val lastChar = state.equation.lastOrNull()
            if (lastChar != null && (lastChar.isDigit() || lastChar == '.')) {
                val newEq = state.equation + "%"
                state.copy(equation = newEq, displayValue = newEq)
            } else {
                state
            }
        }
    }

    private fun handleClear() {
        _uiState.update { CalculatorUiState() }
    }

    private fun handleEvaluate() {
        _uiState.update { state ->
            if (state.equation.isEmpty() || state.hasError) return@update state
            
            val expressionToEvaluate = state.equation
            val result = engine.evaluate(expressionToEvaluate)
            val isErr = result.startsWith("Error")
            
            state.copy(
                equation = if (isErr) "0" else result,
                displayValue = result,
                historyExpression = if (isErr) "" else expressionToEvaluate,
                isOutputResult = true,
                hasError = isErr
            )
        }
    }
}
