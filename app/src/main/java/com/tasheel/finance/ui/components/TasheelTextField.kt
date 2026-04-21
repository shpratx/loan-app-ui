package com.tasheel.finance.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tasheel.finance.ui.theme.Dimens
import com.tasheel.finance.ui.theme.TasheelTheme

// Input aligned to input.component.scss:
// border: 2px solid #858aad (outlineVariant), border-radius: 8px (radiusLg), height: 48dp

@Composable
fun TasheelTextField(
    value: String, onValueChange: (String) -> Unit, label: String,
    modifier: Modifier = Modifier, error: String? = null, enabled: Boolean = true, readOnly: Boolean = false,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value, onValueChange = onValueChange,
            label = { Text(label, style = MaterialTheme.typography.labelMedium) },
            isError = error != null, enabled = enabled, readOnly = readOnly,
            modifier = Modifier.fillMaxWidth().heightIn(min = Dimens.inputHeight),
            shape = RoundedCornerShape(Dimens.radiusLg),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
            ),
            supportingText = error?.let { { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall) } },
            singleLine = true,
        )
    }
}

@Preview @Composable private fun PreviewDefault() = TasheelTheme { TasheelTextField("Ahmed", onValueChange = {}, label = "Full Name") }
@Preview @Composable private fun PreviewError() = TasheelTheme { TasheelTextField("", onValueChange = {}, label = "Full Name", error = "Required") }
