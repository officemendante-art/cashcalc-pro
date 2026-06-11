package com.example.core.navigation

sealed class Screen(val route: String) {
    object Calculator : Screen("calculator")
    object CurrencyCounter : Screen("currency_counter")
}
