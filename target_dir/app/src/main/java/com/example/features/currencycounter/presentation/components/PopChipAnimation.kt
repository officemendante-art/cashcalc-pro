package com.example.features.currencycounter.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@Composable
fun PopChipAnimation(
    denominationValue: Int,
    hexColor: Long,
    onComplete: () -> Unit
) {
    val offsetY = remember { Animatable(30f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        launch {
            offsetY.animateTo(
                targetValue = -30f,
                animationSpec = tween(durationMillis = 600)
            )
        }
        launch {
            delay(300)
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 300)
            )
            onComplete()
        }
    }

    DenominationChip(
        denominationValue = denominationValue,
        hexColor = hexColor,
        modifier = Modifier
            .offset(y = offsetY.value.dp)
            .graphicsLayer { this.alpha = alpha.value }
    )
}
