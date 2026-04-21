package com.tasheel.finance.ui.application

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.tasheel.finance.ui.components.TasheelPrimaryButton
import com.tasheel.finance.ui.components.TasheelTextField
import com.tasheel.finance.ui.theme.Dimens
import com.tasheel.finance.ui.theme.TasheelTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardCollectionScreen(
    applicationId: String, onCardRegistered: (String) -> Unit, onBack: () -> Unit,
    viewModel: CardCollectionViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state.success) { if (state.success) onCardRegistered(applicationId) }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Card & Auto-Debit Setup") },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } })
    }) { padding ->
        Column(Modifier.padding(padding).padding(Dimens.lg)) {
            Text("Debit Card Details", style = MaterialTheme.typography.titleMedium)
            Text("Simulated hosted fields – card data never touches our servers", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(Dimens.lg))
            TasheelTextField(state.cardNumber, { viewModel.update { copy(cardNumber = it) } }, "Card Number")
            Spacer(Modifier.height(Dimens.md))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Dimens.md)) {
                TasheelTextField(state.expiryMonth, { viewModel.update { copy(expiryMonth = it) } }, "MM", Modifier.weight(1f))
                TasheelTextField(state.expiryYear, { viewModel.update { copy(expiryYear = it) } }, "YY", Modifier.weight(1f))
                TasheelTextField(state.cvv, { viewModel.update { copy(cvv = it) } }, "CVV", Modifier.weight(1f))
            }
            Spacer(Modifier.height(Dimens.xl))
            // Single-date auto-debit setup (pre-BridgeNow)
            Text("Auto-Debit Schedule", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(Dimens.sm))
            Text("Choose a single monthly debit date", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(Dimens.md))
            TasheelTextField(state.autoDebitDay, { viewModel.update { copy(autoDebitDay = it) } }, "Debit Day (1–28)")
            Spacer(Modifier.height(Dimens.xl))
            state.error?.let { Text(it, color = MaterialTheme.colorScheme.error); Spacer(Modifier.height(Dimens.sm)) }
            TasheelPrimaryButton(
                text = if (state.isSubmitting) "Registering..." else "Register Card & Continue",
                onClick = { viewModel.registerCard(applicationId) }, modifier = Modifier.fillMaxWidth(), enabled = !state.isSubmitting,
            )
        }
    }
}

@Preview @Composable private fun PreviewCard() = TasheelTheme {
    Column(Modifier.padding(Dimens.lg)) {
        Text("Card & Auto-Debit Setup", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(Dimens.lg))
        TasheelTextField("", {}, "Card Number")
    }
}
