package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.core.navigation.NavigationGraph
import com.example.features.calculator.presentation.CalculatorViewModel
import com.example.features.currencycounter.presentation.CurrencyCounterViewModel
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.BackgroundOffWhite

class MainActivity : ComponentActivity() {
    
    // Instantiate our core viewmodels without heavy dependency injection frameworks
    private val calculatorViewModel by viewModels<CalculatorViewModel>()
    private val currencyCounterViewModel by viewModels<CurrencyCounterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Match edge-to-edge guideline requirements
        enableEdgeToEdge()
        
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundOffWhite
                ) {
                    NavigationGraph(
                        calculatorViewModel = calculatorViewModel,
                        currencyViewModel = currencyCounterViewModel
                    )
                }
            }
        }
    }
}
