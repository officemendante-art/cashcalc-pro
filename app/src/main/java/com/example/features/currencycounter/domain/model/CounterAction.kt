package com.example.features.currencycounter.domain.model

sealed interface CounterAction {
    data class Increment(val denomination: Int) : CounterAction
    data class Decrement(val denomination: Int) : CounterAction
    data class UpdateValue(val denomination: Int, val count: Int) : CounterAction
    data class BulkAdd(val denomination: Int, val count: Int) : CounterAction
    data class BulkReduce(val denomination: Int, val count: Int) : CounterAction
    data class ResetDenomination(val denomination: Int) : CounterAction
    object Reset : CounterAction
}
