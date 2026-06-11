package com.example.core.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.BorderLight
import com.example.ui.theme.SoftShadowDark
import com.example.ui.theme.SoftShadowLight
import com.example.ui.theme.EqualButtonCharcoal
import com.example.ui.theme.NumberButtonWhite

@Composable
fun SoftTactileButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    textColor: Color = DarkCharcoal,
    isBold: Boolean = false,
    fontSize: Int = 22,
    useMonospace: Boolean = false,
    testTag: String = "",
    glowColor: Color? = null
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Smooth spring responsive scale
    val scaleFactor by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(dampingRatio = 0.62f, stiffness = 1200f),
        label = "ButtonScale"
    )

    // Smooth physical mechanical displacement travel offset
    val travelOffset by animateFloatAsState(
        targetValue = if (isPressed) -1.0f else 0.0f, // Slight upward bump instead of deep press based on user tiny elevation increase
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 1300f),
        label = "ButtonTravel"
    )

    // Smooth physics bloom animation for the gorgeous glow
    val glowIntensity by animateFloatAsState(
        targetValue = if (isPressed) 1.0f else 0.0f,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 1500f),
        label = "GlowIntensity"
    )

    // Classify the button category precisely
    val isOperator = text in listOf("÷", "×", "-", "+")
    val isEqual = backgroundColor == EqualButtonCharcoal || text == "="

    // Determine the very faint inner illumination/glow color palette 
    val targetGlowColor = glowColor ?: when {
        isEqual -> Color(0xFFFFFFFF)      // Pure white ambient edge
        isOperator -> Color(0xFFF0F0F5)   // Cool white edge light
        else -> null
    }

    // Dynamic text color under active emission
    val activeTextColor = if (isPressed) {
        when {
            isEqual -> Color.White
            isOperator -> DarkCharcoal // Keep text crisp high contrast
            else -> textColor                
        }
    } else {
        textColor
    }

    Surface(
        modifier = modifier
            .scale(scaleFactor)
            .offset(y = travelOffset.dp) // Beautiful physical downward keyboard travel offset
            .padding(6.dp)
            .testTag(testTag),
        shape = RoundedCornerShape(28.dp), // Organic premium rounded corner
        color = backgroundColor, // Kept stable; inner highlight handles the brightening
        shadowElevation = if (isPressed) 1.dp else 3.5.dp, // Outer shadow reduces on press (sink inward)
        tonalElevation = if (isPressed) 0.dp else 0.dp,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isPressed) 1.5.dp else 1.2.dp, // Slightly stronger edge contrast
            color = if (isPressed && targetGlowColor != null) {
                targetGlowColor.copy(alpha = 0.5f) // The actual "edge light" hitting the rim
            } else if (isPressed) {
                BorderLight.copy(alpha = 0.8f)
            } else {
                BorderLight
            }
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    if (glowIntensity > 0.01f && targetGlowColor != null) {
                        // Soft surface brightening (illuminated surface)
                        drawRoundRect(
                            color = targetGlowColor.copy(alpha = 0.08f * glowIntensity),
                            size = size,
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(28.dp.toPx(), 28.dp.toPx())
                        )
                        
                        // Fake inner shadow (gradient stroke along the edge to simulate depth sinking inside)
                        drawRoundRect(
                            color = Color.Black.copy(alpha = 0.04f * glowIntensity),
                            size = size,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx()),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(28.dp.toPx(), 28.dp.toPx())
                        )
                    } else if (isPressed) {
                        // Standard inner shadow for normal buttons sinking inward
                        drawRoundRect(
                            color = Color.Black.copy(alpha = 0.03f),
                            size = size,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx()),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(28.dp.toPx(), 28.dp.toPx())
                        )
                    }
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null, // Custom physical scale and haptic clicks used instead of default ripples
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onClick()
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = activeTextColor,
                fontSize = fontSize.sp,
                fontWeight = if (isBold) FontWeight.SemiBold else FontWeight.Normal,
                fontFamily = if (useMonospace) FontFamily.Monospace else FontFamily.SansSerif,
                letterSpacing = if (isBold) 0.5.sp else 0.sp,
                modifier = Modifier.offset(y = (travelOffset * 0.4f).dp) // Subtle parallax displacement relative to movement
            )
        }
    }
}

