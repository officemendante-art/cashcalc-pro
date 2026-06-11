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
                .padding(top = 16.dp, bottom = 12.dp),
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
                    .padding(24.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                // Expression History (Muted Grey)
                if (state.historyExpression.isNotEmpty()) {
                    Text(
                        text = state.historyExpression,
                        color = MutedGrey,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
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
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Keypad Grid (Matches premium circular buttons)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Row 1: AC, Backspace, %, ÷
            Row(modifier = Modifier.fillMaxWidth().height(82.dp)) {
                SoftTactileButton(
                    text = "AC",
                    onClick = { viewModel.onAction(CalculatorAction.Clear) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = MutedGrey,
                    isBold = true,
                    fontSize = 18,
                    testTag = "clear_button"
                )
                SoftTactileButton(
                    text = "⌫",
                    onClick = { viewModel.onAction(CalculatorAction.Backspace) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = MutedGrey,
                    isBold = true,
                    fontSize = 18,
                    testTag = "backspace_button"
                )
                SoftTactileButton(
                    text = "%",
                    onClick = { viewModel.onAction(CalculatorAction.Percent) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = NumberButtonWhite,
                    textColor = MutedGrey,
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

