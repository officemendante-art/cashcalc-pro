package com.example.features.currencycounter.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.components.SoftTactileButton
import com.example.features.currencycounter.domain.model.CounterAction
import com.example.features.currencycounter.domain.model.Denomination
import com.example.ui.theme.*

import com.example.features.currencycounter.presentation.components.AnimatedScalingWrapper
import com.example.features.currencycounter.presentation.components.CurrencyLedgerCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyCounterScreen(
    viewModel: CurrencyCounterViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundOffWhite)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        // Shared Premium Brand Header with Calculator Screen
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "PREMIUM",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    color = MutedGrey,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "CashCalc Pro",
                    fontSize = 22.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    color = DarkCharcoal,
                    letterSpacing = (-0.5).sp
                )
            }
            // Elegant Rupee icon avatar
            Surface(
                modifier = Modifier.size(40.dp),
                shape = androidx.compose.foundation.shape.CircleShape,
                color = Color.White,
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "₹",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light,
                        color = DarkCharcoal
                    )
                }
            }
        }

        // Sticky Header: Grand Total Box (Apple-inspired premium dashboard card)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(32.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            border = androidx.compose.foundation.BorderStroke(1.2.dp, BorderLight)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "GRAND TOTAL",
                        style = MaterialTheme.typography.labelLarge,
                        color = MutedGrey,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // Reset Trigger for everything
                    IconButton(
                        onClick = { viewModel.onAction(CounterAction.Reset) },
                        modifier = Modifier
                            .background(BackgroundOffWhite, RoundedCornerShape(12.dp))
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = "Reset Lists",
                            tint = DarkCharcoal,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Numeric readout with Indian grouping
                AnimatedScalingWrapper(
                    valueToWatch = state.totalAmount,
                    peakScale = 1.03f,
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {
                    Text(
                        text = viewModel.formatAmount(state.totalAmount),
                        fontSize = 38.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Light,
                        color = DarkCharcoal
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Display Badge counts
                    AnimatedScalingWrapper(
                        valueToWatch = state.totalNotes,
                        peakScale = 1.1f
                    ) {
                        Text(
                            text = "TOTAL NOTES: ${state.totalNotes}",
                            style = MaterialTheme.typography.labelLarge,
                            color = AccentRed,
                            modifier = Modifier
                                .background(AccentRed.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "INR ACTIVE",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MutedGrey,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Scrollable ledger rows
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(Denomination.IndianRupeeBills) { denom ->
                val currentCount = state.counts[denom.value] ?: 0
                val rowSubtotal = denom.value.toDouble() * currentCount

                CurrencyLedgerCard(
                    denom = denom,
                    currentCount = currentCount,
                    rowSubtotal = rowSubtotal,
                    formatAmount = { viewModel.formatAmount(it) },
                    onAction = { viewModel.onAction(it) }
                )
            }
        }
    }
}
