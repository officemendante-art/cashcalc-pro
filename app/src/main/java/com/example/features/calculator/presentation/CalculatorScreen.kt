package com.example.features.calculator.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.components.SoftTactileButton
import com.example.features.calculator.domain.model.CalculatorAction
import com.example.features.calculator.domain.NumberToWordsConverter
import com.example.ui.theme.*

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundOffWhite)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Premium Brand Header
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
                shape = CircleShape,
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

        // Display Area (Implements neumorphic-inset)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(32.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            border = androidx.compose.foundation.BorderStroke(1.2.dp, BorderLight)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                // Recent History (Last 5)
                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Bottom,
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    if (state.recentHistory.isNotEmpty()) {
                        item {
                            androidx.compose.material3.TextButton(
                                onClick = { viewModel.onAction(CalculatorAction.ClearHistory) },
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                androidx.compose.foundation.layout.Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("Clear History", color = AccentRed, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                    items(state.recentHistory.size) { index ->
                        Text(
                            text = state.recentHistory[index],
                            color = MutedGrey.copy(alpha = 0.6f),
                            fontSize = 18.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )
                    }
                }

                // Expression History (Muted Grey)
                if (state.historyExpression.isNotEmpty()) {
                    Text(
                        text = state.historyExpression,
                        color = MutedGrey,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
                    )
                }

                // Main display val
                Text(
                    text = state.displayValue,
                    color = if (state.hasError) AccentRed else DarkCharcoal,
                    fontSize = if (state.displayValue.length > 12) 36.sp else 48.sp,
                    lineHeight = if (state.displayValue.length > 12) 42.sp else 56.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.End,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                val displayWords = remember(state.displayValue, state.hasError) {
                    if (state.hasError || state.displayValue == "0" || state.displayValue.isEmpty()) "" 
                    else NumberToWordsConverter.convertExpression(state.displayValue)
                }
                
                if (displayWords.isNotEmpty() && displayWords != "zero") {
                    Text(
                        text = displayWords,
                        color = MutedGrey.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Keypad Grid (Matches premium circular buttons)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Row 1: AC, Backspace, %, ÷
            Row(modifier = Modifier.fillMaxWidth().height(82.dp)) {
                SoftTactileButton(
                    text = "AC",
                    onClick = { viewModel.onAction(CalculatorAction.Clear) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    isBold = true,
                    fontSize = 18,
                    testTag = "clear_button"
                )
                SoftTactileButton(
                    text = "⌫",
                    onClick = { viewModel.onAction(CalculatorAction.Backspace) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    isBold = true,
                    fontSize = 18,
                    testTag = "backspace_button"
                )
                SoftTactileButton(
                    text = "%",
                    onClick = { viewModel.onAction(CalculatorAction.Percent) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    isBold = true,
                    fontSize = 18,
                    testTag = "percent_button"
                )
                SoftTactileButton(
                    text = "÷",
                    onClick = { viewModel.onAction(CalculatorAction.Operator("÷")) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    isBold = true,
                    fontSize = 24,
                    testTag = "divide_button"
                )
            }

            // Row 2: 7, 8, 9, ×
            Row(modifier = Modifier.fillMaxWidth().height(82.dp)) {
                SoftTactileButton(
                    text = "7",
                    onClick = { viewModel.onAction(CalculatorAction.Number("7")) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    fontSize = 22,
                    testTag = "num_7"
                )
                SoftTactileButton(
                    text = "8",
                    onClick = { viewModel.onAction(CalculatorAction.Number("8")) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    fontSize = 22,
                    testTag = "num_8"
                )
                SoftTactileButton(
                    text = "9",
                    onClick = { viewModel.onAction(CalculatorAction.Number("9")) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    fontSize = 22,
                    testTag = "num_9"
                )
                SoftTactileButton(
                    text = "×",
                    onClick = { viewModel.onAction(CalculatorAction.Operator("×")) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    isBold = true,
                    fontSize = 24,
                    testTag = "multiply_button"
                )
            }

            // Row 3: 4, 5, 6, -
            Row(modifier = Modifier.fillMaxWidth().height(82.dp)) {
                SoftTactileButton(
                    text = "4",
                    onClick = { viewModel.onAction(CalculatorAction.Number("4")) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    fontSize = 22,
                    testTag = "num_4"
                )
                SoftTactileButton(
                    text = "5",
                    onClick = { viewModel.onAction(CalculatorAction.Number("5")) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    fontSize = 22,
                    testTag = "num_5"
                )
                SoftTactileButton(
                    text = "6",
                    onClick = { viewModel.onAction(CalculatorAction.Number("6")) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    fontSize = 22,
                    testTag = "num_6"
                )
                SoftTactileButton(
                    text = "-",
                    onClick = { viewModel.onAction(CalculatorAction.Operator("-")) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    isBold = true,
                    fontSize = 24,
                    testTag = "subtract_button"
                )
            }

            // Row 4: 1, 2, 3, +
            Row(modifier = Modifier.fillMaxWidth().height(82.dp)) {
                SoftTactileButton(
                    text = "1",
                    onClick = { viewModel.onAction(CalculatorAction.Number("1")) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    fontSize = 22,
                    testTag = "num_1"
                )
                SoftTactileButton(
                    text = "2",
                    onClick = { viewModel.onAction(CalculatorAction.Number("2")) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    fontSize = 22,
                    testTag = "num_2"
                )
                SoftTactileButton(
                    text = "3",
                    onClick = { viewModel.onAction(CalculatorAction.Number("3")) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    fontSize = 22,
                    testTag = "num_3"
                )
                SoftTactileButton(
                    text = "+",
                    onClick = { viewModel.onAction(CalculatorAction.Operator("+")) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    isBold = true,
                    fontSize = 24,
                    testTag = "add_button"
                )
            }

            // Row 5: 0, ., =
            Row(modifier = Modifier.fillMaxWidth().height(82.dp)) {
                SoftTactileButton(
                    text = "0",
                    onClick = { viewModel.onAction(CalculatorAction.Number("0")) },
                    modifier = Modifier.weight(2f), // Double width 0
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    fontSize = 22,
                    testTag = "num_0"
                )
                SoftTactileButton(
                    text = ".",
                    onClick = { viewModel.onAction(CalculatorAction.Decimal) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = DarkCharcoal,
                    fontSize = 22,
                    testTag = "decimal_button"
                )
                SoftTactileButton(
                    text = "=",
                    onClick = { viewModel.onAction(CalculatorAction.Evaluate) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = EqualButtonCharcoal,
                    textColor = Color.White,
                    isBold = true,
                    fontSize = 26,
                    testTag = "equals_button"
                )
            }
        }
    }
}

