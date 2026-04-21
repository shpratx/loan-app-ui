package com.tasheel.finance.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tasheel.finance.ui.theme.Dimens
import com.tasheel.finance.ui.theme.TasheelTheme

// Card aligned to cards.component.scss:
// border-radius: 6px (--awe-card-border-radius), shadow: 0 2px 4px rgba(0,0,0,0.16)
// light: bg N-100 (#ededf3), text N-1000 (#292c3d)

@Composable
fun TasheelCard(modifier: Modifier = Modifier, onClick: (() -> Unit)? = null, content: @Composable () -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth().then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        shape = RoundedCornerShape(Dimens.radiusMd),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.xs),
    ) {
        Column(Modifier.padding(Dimens.xl)) { content() }
    }
}

@Preview @Composable private fun PreviewCard() = TasheelTheme {
    TasheelCard { Text("Cash Finance", style = MaterialTheme.typography.titleMedium) }
}
