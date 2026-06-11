package com.example.features.calculator.presentation

import androidx.lifecycle.ViewModel
import com.example.features.calculator.domain.CalculatorEngine
import com.example.features.calculator.domain.model.CalculatorAction
import com.example.features.calculator.domain.model.CalculatorUiState
import com.example.features.currencycounter.domain.CurrencyFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalculatorViewModel : ViewModel() {

    private val engine = CalculatorEngine()
    private val formatter = CurrencyFormatter()
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
            is CalculatorAction.ClearHistory -> handleClearHistory()
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
                state.copy(equation = newEquation, displayValue = formatter.formatExpression(newEquation))
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
                state.copy(equation = newEquation, displayValue = formatter.formatExpression(newEquation), isOutputResult = false)
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
                state.copy(equation = newEq, displayValue = formatter.formatExpression(newEq), isOutputResult = false)
            } else {
                state
            }
        }
    }

    private fun handleBackspace() {
        _uiState.update { state ->
            if (state.hasError || state.isOutputResult) {
                CalculatorUiState(recentHistory = state.recentHistory)
            } else {
                val newEq = if (state.equation.length <= 1) "0" else state.equation.dropLast(1)
                state.copy(equation = newEq, displayValue = formatter.formatExpression(newEq))
            }
        }
    }

    private fun handlePercent() {
        _uiState.update { state ->
            if (state.hasError || state.equation.isEmpty()) return@update state
            val lastChar = state.equation.lastOrNull()
            if (lastChar != null && (lastChar.isDigit() || lastChar == '.')) {
                val newEq = state.equation + "%"
                state.copy(equation = newEq, displayValue = formatter.formatExpression(newEq))
            } else {
                state
            }
        }
    }

    private fun handleClear() {
        _uiState.update { state -> CalculatorUiState(recentHistory = state.recentHistory) }
    }

    private fun handleClearHistory() {
        _uiState.update { state -> state.copy(recentHistory = emptyList()) }
    }

    private fun handleEvaluate() {
        _uiState.update { state ->
            if (state.equation.isEmpty() || state.hasError) return@update state
            
            val expressionToEvaluate = state.equation
            val result = engine.evaluate(expressionToEvaluate)
            val isErr = result.startsWith("Error")
            
            val formattedExpr = if (isErr) "" else formatter.formatExpression(expressionToEvaluate)
            val formattedRes = if (isErr) result else formatter.formatExpression(result)
            
            val newHistory = if (!isErr && result != expressionToEvaluate) {
                 (state.recentHistory + "$formattedExpr = $formattedRes").takeLast(5)
            } else {
                 state.recentHistory
            }
            
            state.copy(
                equation = if (isErr) "0" else result,
                displayValue = formattedRes,
                historyExpression = formattedExpr,
                isOutputResult = true,
                hasError = isErr,
                recentHistory = newHistory
            )
        }
    }
}
