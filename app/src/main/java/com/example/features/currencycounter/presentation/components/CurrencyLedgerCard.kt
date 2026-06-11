package com.example.features.currencycounter.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.features.currencycounter.domain.model.CounterAction
import com.example.features.currencycounter.domain.model.Denomination
import com.example.ui.theme.BackgroundOffWhite
import com.example.ui.theme.BorderLight
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.MutedGrey
import kotlinx.coroutines.delay

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CurrencyLedgerCard(
    denom: Denomination,
    currentCount: Int,
    rowSubtotal: Double,
    formatAmount: (Double) -> String,
    onAction: (CounterAction) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val cardScale by animateFloatAsState(targetValue = if (isPressed) 0.98f else 1f)
    var popEffects by remember { mutableStateOf(listOf<Long>()) }

    var bulkMode by remember { mutableStateOf(0) } // 0=none, 1=add, 2=reduce
    var bulkInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    fun triggerPop() {
        popEffects = popEffects + System.currentTimeMillis()
    }

    Box {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    scaleX = cardScale
                    scaleY = cardScale
                }
                .pointerInput(denom.value) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitFirstDown(requireUnconsumed = false)
                            isPressed = true
                            waitForUpOrCancellation()
                            isPressed = false
                        }
                    }
                }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                // Left Section: Chip, Layout
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1.3f)
                ) {
                    DenominationChip(
                        denominationValue = denom.value,
                        hexColor = denom.hexColor,
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            triggerPop()
                            onAction(CounterAction.Increment(denom.value))
                        }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "${denom.label} Note",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkCharcoal
                        )
                        AnimatedScalingWrapper(
                            valueToWatch = rowSubtotal,
                            peakScale = 1.05f
                        ) {
                            Text(
                                text = formatAmount(rowSubtotal),
                                style = LocalTextStyle.current.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                color = MutedGrey
                            )
                        }
                    }
                }

                // Right Section: Numeric Controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.weight(1.7f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(BackgroundOffWhite, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .combinedClickable(
                                onClick = { onAction(CounterAction.Decrement(denom.value)) },
                                onLongClick = {
                                    bulkMode = if (bulkMode == 2) 0 else 2
                                    bulkInput = ""
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("-", fontSize = 18.sp, color = DarkCharcoal, fontWeight = FontWeight.Medium)
                    }

                    AnimatedScalingWrapper(
                        valueToWatch = currentCount,
                        peakScale = 1.12f,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        OutlinedTextField(
                            value = if (currentCount == 0) "" else currentCount.toString(),
                            onValueChange = { input: String ->
                                val cleaned = input.filter { it.isDigit() }
                                val parsed = cleaned.toIntOrNull() ?: 0
                                onAction(CounterAction.UpdateValue(denom.value, parsed))
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            ),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 15.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Medium,
                                color = DarkCharcoal
                            ),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(denom.hexColor),
                                unfocusedBorderColor = BorderLight,
                                focusedContainerColor = BackgroundOffWhite,
                                unfocusedContainerColor = BackgroundOffWhite
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .width(64.dp)
                                .padding(vertical = 2.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(BackgroundOffWhite, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .combinedClickable(
                                onClick = {
                                    triggerPop()
                                    onAction(CounterAction.Increment(denom.value))
                                },
                                onLongClick = {
                                    bulkMode = if (bulkMode == 1) 0 else 1
                                    bulkInput = ""
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", fontSize = 18.sp, color = DarkCharcoal, fontWeight = FontWeight.Medium)
                    }
                }
            } // Close main row

            // Inline Bulk Input Section
            AnimatedVisibility(visible = bulkMode != 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundOffWhite)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (bulkMode == 1) "Bulk Add" else "Bulk Reduce",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkCharcoal
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = bulkInput,
                            onValueChange = { input -> bulkInput = input.filter { it.isDigit() } },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            ),
                            singleLine = true,
                            placeholder = { Text("qty", fontSize = 13.sp, color = MutedGrey) },
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 14.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                fontFamily = FontFamily.Monospace
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(denom.hexColor),
                                unfocusedBorderColor = BorderLight,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .width(80.dp)
                                .height(48.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                val amount = bulkInput.toIntOrNull() ?: 0
                                if (amount > 0) {
                                    if (bulkMode == 1) {
                                        onAction(CounterAction.BulkAdd(denom.value, amount))
                                    } else {
                                        onAction(CounterAction.BulkReduce(denom.value, amount))
                                    }
                                }
                                bulkMode = 0
                                bulkInput = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkCharcoal),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text("Apply", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } // Close column
        } // Close Card

        // Overlay popping chips
        popEffects.forEach { popId ->
            Box(
                modifier = Modifier.matchParentSize(),
                contentAlignment = Alignment.CenterStart  // align to vertical center where chip lives
            ) {
                Box(modifier = Modifier.padding(start = 12.dp)) {  // horizontal offset matches chip left padding
                    PopChipAnimation(
                        denominationValue = denom.value,
                        hexColor = denom.hexColor,
                        onComplete = { popEffects = popEffects.filter { it != popId } }
                    )
                }
            }
        }
    }
}
