package com.example.features.currencycounter.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PopChipAnimation(
    denominationValue: Int,
    hexColor: Long,
    onComplete: () -> Unit
) {
    val offsetY = remember { Animatable(-10f) }   // spawn already above chip
    val alpha = remember { Animatable(1f) }
    val scale = remember { Animatable(0.75f) }    // pops in from small

    LaunchedEffect(Unit) {
        // Spring pop-in scale
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(dampingRatio = 0.5f, stiffness = 800f)
            )
        }
        // Float upward — slow and floaty like a bubble
        launch {
            offsetY.animateTo(
                targetValue = -72f,
                animationSpec = spring(
                    dampingRatio = 1f,      // no bounce on exit, just clean float
                    stiffness = 160f
                )
            )
        }
        // Fade out after short delay
        launch {
            delay(220)
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing)
            )
            onComplete()
        }
    }

    DenominationChip(
        denominationValue = denominationValue,
        hexColor = hexColor,
        modifier = Modifier
            .offset(y = offsetY.value.dp)
            .graphicsLayer {
                this.alpha = alpha.value
                this.scaleX = scale.value
                this.scaleY = scale.value
            }
    )
}
