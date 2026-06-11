package com.example.core.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.features.calculator.presentation.CalculatorScreen
import com.example.features.calculator.presentation.CalculatorViewModel
import com.example.features.currencycounter.presentation.CurrencyCounterScreen
import com.example.features.currencycounter.presentation.CurrencyCounterViewModel
import com.example.ui.theme.BackgroundOffWhite
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NavigationGraph(
    calculatorViewModel: CalculatorViewModel,
    currencyViewModel: CurrencyCounterViewModel
) {
    // Initial page set to 0 (Calculator)
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundOffWhite
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                beyondViewportPageCount = 0
            ) { page ->
                // Calculate precise offset of page from the current screen view
                val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).toFloat()
                
                // Polish dynamic properties
                val scale = 1f - (abs(pageOffset) * 0.02f).coerceIn(0f, 1f) // slight page scale effect: 0.98 to 1.00
                val alpha = 1f - (abs(pageOffset) * 0.35f).coerceIn(0f, 1f) // inactive page opacity around 0.65
                val parallaxTranslationX = pageOffset * 60f // subtle parallax without breaking bounds

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clipToBounds() // Strictly clip to static bounds so sliding content never leaks visually!
                        .background(BackgroundOffWhite) // Fill static page background 
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                this.alpha = alpha
                                this.translationX = parallaxTranslationX
                            }
                    ) {
                        when (page) {
                            0 -> CalculatorScreen(viewModel = calculatorViewModel)
                            1 -> CurrencyCounterScreen(viewModel = currencyViewModel)
                        }
                    }
                }
            }

            // A tiny, almost invisible arrow partially visible on the right edge of Calculator page
            if (pagerState.currentPage == 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 6.dp)
                        .graphicsLayer {
                            // Fades the hint out completely as the user drags towards page 1
                            alpha = (1f - pagerState.currentPageOffsetFraction).coerceIn(0f, 1f) * 0.12f
                        }
                ) {
                    SubtleArrowHint(directionRight = true)
                }
            }

            // A tiny, almost invisible arrow partially visible on the left edge of Cash Counter page
            if (pagerState.currentPage == 1) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 6.dp)
                        .graphicsLayer {
                            // Fades the hint out completely as the user drags back towards page 0
                            alpha = (pagerState.currentPageOffsetFraction).coerceIn(0f, 1f) * 0.12f
                        }
                ) {
                    SubtleArrowHint(directionRight = false)
                }
            }
        }
    }
}

@Composable
private fun SubtleArrowHint(directionRight: Boolean, modifier: Modifier = Modifier) {
    androidx.compose.foundation.Canvas(modifier = modifier.size(10.dp, 20.dp)) {
        val path = androidx.compose.ui.graphics.Path().apply {
            if (directionRight) {
                moveTo(2.dp.toPx(), 4.dp.toPx())
                lineTo(8.dp.toPx(), 10.dp.toPx())
                lineTo(2.dp.toPx(), 16.dp.toPx())
            } else {
                moveTo(8.dp.toPx(), 4.dp.toPx())
                lineTo(2.dp.toPx(), 10.dp.toPx())
                lineTo(8.dp.toPx(), 16.dp.toPx())
            }
        }
        drawPath(
            path = path,
            color = Color(0xFF1C1C1E), // Premium charcoal hint (opacity controlled via graphicsLayer)
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = 1.8.dp.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round,
                join = androidx.compose.ui.graphics.StrokeJoin.Round
            )
        )
    }
}
