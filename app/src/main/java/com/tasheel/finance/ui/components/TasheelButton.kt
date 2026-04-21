package com.tasheel.finance.ui.components

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tasheel.finance.ui.theme.Dimens
import com.tasheel.finance.ui.theme.TasheelTheme

// Button variants aligned to button.component.scss: primary, secondary, text
// border-radius: --radius-sm (4dp), min height: 48dp (touch target)

@Composable
fun TasheelPrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    Button(
        onClick = onClick, enabled = enabled,
        modifier = modifier.heightIn(min = Dimens.minTouchTarget),
        shape = RoundedCornerShape(Dimens.radiusSm),
    ) { Text(text, style = MaterialTheme.typography.labelLarge) }
}

@Composable
fun TasheelSecondaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    OutlinedButton(
        onClick = onClick, enabled = enabled,
        modifier = modifier.heightIn(min = Dimens.minTouchTarget),
        shape = RoundedCornerShape(Dimens.radiusSm),
    ) { Text(text, style = MaterialTheme.typography.labelLarge) }
}

@Composable
fun TasheelTextButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(
        onClick = onClick,
        modifier = modifier.heightIn(min = Dimens.minTouchTarget),
    ) { Text(text, style = MaterialTheme.typography.labelLarge) }
}

@Preview @Composable private fun PreviewLight() = TasheelTheme(darkTheme = false) { TasheelPrimaryButton("Apply Now", onClick = {}) }
@Preview @Composable private fun PreviewDark() = TasheelTheme(darkTheme = true) { TasheelPrimaryButton("Apply Now", onClick = {}) }
