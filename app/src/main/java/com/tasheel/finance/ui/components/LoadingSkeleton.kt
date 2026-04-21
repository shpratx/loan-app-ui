package com.tasheel.finance.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import com.tasheel.finance.ui.theme.*

// Skeleton loading per EA11: use skeleton composable, not spinner
// Shimmer animation aligned to button.component.scss @keyframes skeleton-loading

@Composable
fun LoadingSkeleton(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val offset by transition.animateFloat(0f, 1000f, infiniteRepeatable(tween(1400, easing = LinearEasing)), label = "shimmer")
    val brush = Brush.linearGradient(listOf(Neutral100, Neutral200, Neutral100), start = Offset(offset, 0f), end = Offset(offset + 200f, 0f))
    Column(modifier.padding(Dimens.xl), verticalArrangement = Arrangement.spacedBy(Dimens.md)) {
        Box(Modifier.fillMaxWidth().height(Dimens.xl).background(brush, RoundedCornerShape(Dimens.radiusSm)))
        Box(Modifier.fillMaxWidth(0.7f).height(Dimens.lg).background(brush, RoundedCornerShape(Dimens.radiusSm)))
        Box(Modifier.fillMaxWidth(0.5f).height(Dimens.lg).background(brush, RoundedCornerShape(Dimens.radiusSm)))
    }
}

@Preview @Composable private fun PreviewSkeleton() = TasheelTheme { LoadingSkeleton() }
