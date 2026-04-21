package com.tasheel.finance.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.tasheel.finance.ui.theme.Dimens
import com.tasheel.finance.ui.theme.TasheelTheme

// Error state per EA2: user-friendly message + retry action
// "Connection issue — your progress is saved. Tap to retry."

@Composable
fun ErrorState(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().padding(Dimens.xxxl), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("⚠️", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(Dimens.lg))
        Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        Spacer(Modifier.height(Dimens.xl))
        TasheelPrimaryButton("Retry", onClick = onRetry)
    }
}

@Preview @Composable private fun PreviewError() = TasheelTheme { ErrorState("Connection issue — your progress is saved.", onRetry = {}) }
