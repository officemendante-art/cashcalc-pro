package com.example.features.currencycounter.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay

@Composable
fun AnimatedScalingWrapper(
    valueToWatch: Any?,
    modifier: Modifier = Modifier,
    peakScale: Float = 1.12f,
    durationMs: Int = 120,
    content: @Composable () -> Unit
) {
    var trigger by remember { mutableStateOf(false) }

    LaunchedEffect(valueToWatch) {
        trigger = true
        delay(durationMs.toLong() / 2)
        trigger = false
    }

    val scale by animateFloatAsState(
        targetValue = if (trigger) peakScale else 1f,
        animationSpec = tween(durationMs / 2),
        label = "SubtleScaleBump"
    )

    Box(
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
    ) {
        content()
    }
}

