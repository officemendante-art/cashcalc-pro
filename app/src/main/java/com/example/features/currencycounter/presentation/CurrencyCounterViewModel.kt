package com.example.features.currencycounter.presentation

import androidx.lifecycle.ViewModel
import com.example.features.currencycounter.domain.CurrencyFormatter
import com.example.features.currencycounter.domain.model.CounterAction
import com.example.features.currencycounter.domain.model.CurrencyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CurrencyCounterViewModel : ViewModel() {

    private val formatter = CurrencyFormatter()
    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    fun onAction(action: CounterAction) {
        when (action) {
            is CounterAction.Increment -> {
                _uiState.update { state ->
                    val currentCount = state.counts[action.denomination] ?: 0
                    val newCounts = state.counts.toMutableMap().apply {
                        put(action.denomination, currentCount + 1)
                    }
                    deriveState(newCounts)
                }
            }
            is CounterAction.Decrement -> {
                _uiState.update { state ->
                    val currentCount = state.counts[action.denomination] ?: 0
                    if (currentCount <= 0) return@update state
                    val newCounts = state.counts.toMutableMap().apply {
                        put(action.denomination, currentCount - 1)
                    }
                    deriveState(newCounts)
                }
            }
            is CounterAction.UpdateValue -> {
                _uiState.update { state ->
                    val finalCount = action.count.coerceAtLeast(0)
                    val newCounts = state.counts.toMutableMap().apply {
                        put(action.denomination, finalCount)
                    }
                    deriveState(newCounts)
                }
            }
            is CounterAction.BulkAdd -> {
                _uiState.update { state ->
                    val currentCount = state.counts[action.denomination] ?: 0
                    val newCounts = state.counts.toMutableMap().apply {
                        put(action.denomination, currentCount + action.count)
                    }
                    deriveState(newCounts)
                }
            }
            is CounterAction.BulkReduce -> {
                _uiState.update { state ->
                    val currentCount = state.counts[action.denomination] ?: 0
                    val finalCount = (currentCount - action.count).coerceAtLeast(0)
                    val newCounts = state.counts.toMutableMap().apply {
                        put(action.denomination, finalCount)
                    }
                    deriveState(newCounts)
                }
            }
            is CounterAction.ResetDenomination -> {
                _uiState.update { state ->
                    val newCounts = state.counts.toMutableMap().apply {
                        put(action.denomination, 0)
                    }
                    deriveState(newCounts)
                }
            }
            CounterAction.Reset -> {
                _uiState.update { CurrencyUiState() }
            }
        }
    }

    private fun deriveState(counts: Map<Int, Int>): CurrencyUiState {
        var sum = 0.0
        var totalCount = 0
        for ((denom, count) in counts) {
            sum += denom.toDouble() * count
            totalCount += count
        }
        return CurrencyUiState(
            counts = counts,
            totalAmount = sum,
            totalNotes = totalCount
        )
    }

    fun formatAmount(amount: Double): String {
        return formatter.formatRupee(amount)
    }
}
