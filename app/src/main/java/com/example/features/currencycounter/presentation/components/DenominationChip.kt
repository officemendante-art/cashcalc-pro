package com.example.features.currencycounter.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkCharcoal

@Composable
fun DenominationChip(
    denominationValue: Int,
    hexColor: Long,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color(hexColor).copy(alpha = 0.25f), RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = "₹$denominationValue",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = DarkCharcoal
        )
    }
}
