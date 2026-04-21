package com.tasheel.finance.ui.offer

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.tasheel.finance.core.UiState
import com.tasheel.finance.ui.components.*
import com.tasheel.finance.ui.theme.Dimens
import com.tasheel.finance.ui.theme.TasheelTheme
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferScreen(
    applicationId: String, onComplete: () -> Unit, onBack: () -> Unit,
    viewModel: OfferViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val accepted by viewModel.accepted.collectAsState()
    LaunchedEffect(applicationId) { viewModel.load(applicationId) }
    LaunchedEffect(accepted) { if (accepted) onComplete() }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Your Offer") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } })
    }) { padding ->
        Column(Modifier.padding(padding).padding(Dimens.lg)) {
            when (val s = state) {
                is UiState.Loading -> LoadingSkeletonList(2)
                is UiState.Error -> ErrorState(s.message, onRetry = { viewModel.load(applicationId) })
                is UiState.Success -> OfferContent(s.data, onAccept = { viewModel.accept(s.data.id) }, onDecline = { viewModel.decline(s.data.id) })
            }
        }
    }
}

@Composable
private fun OfferContent(offer: com.tasheel.finance.domain.model.Offer, onAccept: () -> Unit, onDecline: () -> Unit) {
    val fmt = NumberFormat.getNumberInstance(Locale("en", "SA"))
    Text("Congratulations!", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
    Spacer(Modifier.height(Dimens.lg))
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(Dimens.lg)) {
            listOf("Approved Amount" to "SAR ${fmt.format(offer.approvedAmount)}", "Tenure" to "${offer.tenure} months",
                "Interest Rate" to "${offer.interestRate}%", "Monthly Payment" to "SAR ${fmt.format(offer.monthlyPayment)}",
                "Total Repayment" to "SAR ${fmt.format(offer.totalRepayment)}",
            ).forEach { (label, value) ->
                Row(Modifier.fillMaxWidth().padding(vertical = Dimens.xs), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(value, style = MaterialTheme.typography.titleSmall)
                }
            }
        }
    }
    Spacer(Modifier.height(Dimens.xl))
    TasheelPrimaryButton("Accept Offer", onClick = onAccept, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(Dimens.md))
    TasheelSecondaryButton("Decline", onClick = onDecline, modifier = Modifier.fillMaxWidth())
}

@Preview @Composable private fun PreviewOffer() = TasheelTheme {
    Column(Modifier.padding(Dimens.lg)) { Text("Congratulations!", style = MaterialTheme.typography.headlineSmall) }
}
