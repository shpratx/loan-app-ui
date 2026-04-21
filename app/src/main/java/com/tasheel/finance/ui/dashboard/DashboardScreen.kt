package com.tasheel.finance.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.tasheel.finance.core.UiState
import com.tasheel.finance.domain.model.Loan
import com.tasheel.finance.domain.model.Payment
import com.tasheel.finance.ui.components.*
import com.tasheel.finance.ui.theme.Dimens
import com.tasheel.finance.ui.theme.TasheelTheme
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onBack: () -> Unit, viewModel: DashboardViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = { Text("My Dashboard") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } })
    }) { padding ->
        Column(Modifier.padding(padding).padding(Dimens.lg).verticalScroll(rememberScrollState())) {
            when (val s = state) {
                is UiState.Loading -> LoadingSkeletonList()
                is UiState.Error -> ErrorState(s.message, onRetry = viewModel::load)
                is UiState.Success -> DashboardContent(s.data, viewModel::requestSettlement, viewModel::requestLiabilityLetter)
            }
        }
    }
}

@Composable
private fun DashboardContent(data: DashboardData, onSettlement: (String) -> Unit, onLiability: (String) -> Unit) {
    val fmt = NumberFormat.getNumberInstance(Locale("en", "SA"))
    Text("Active Loans", style = MaterialTheme.typography.headlineSmall)
    Spacer(Modifier.height(Dimens.lg))
    if (data.loans.isEmpty()) {
        Text("No active loans", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    data.loans.forEach { loan -> LoanCard(loan, fmt, data.payments[loan.id].orEmpty(), onSettlement, onLiability); Spacer(Modifier.height(Dimens.md)) }
    data.settlementAmount?.let {
        Spacer(Modifier.height(Dimens.lg))
        TasheelStatusCard("Settlement Figure", "SAR ${fmt.format(it)}", Modifier.fillMaxWidth())
    }
}

@Composable
private fun LoanCard(loan: Loan, fmt: NumberFormat, payments: List<Payment>, onSettlement: (String) -> Unit, onLiability: (String) -> Unit) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(Dimens.lg)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Loan", style = MaterialTheme.typography.titleMedium)
                Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.small) {
                    Text(loan.status, Modifier.padding(horizontal = Dimens.sm, vertical = Dimens.xs), style = MaterialTheme.typography.labelSmall)
                }
            }
            Spacer(Modifier.height(Dimens.md))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TasheelStatusCard("Outstanding", "SAR ${fmt.format(loan.outstandingBalance)}", Modifier.weight(1f))
                Spacer(Modifier.width(Dimens.sm))
                TasheelStatusCard("Monthly", "SAR ${fmt.format(loan.monthlyPayment)}", Modifier.weight(1f))
            }
            Spacer(Modifier.height(Dimens.md))
            Text("Next payment: ${loan.nextPaymentDate}", style = MaterialTheme.typography.bodySmall)
            if (payments.isNotEmpty()) {
                Spacer(Modifier.height(Dimens.md))
                Text("Recent Payments", style = MaterialTheme.typography.labelLarge)
                payments.take(3).forEach { p ->
                    Row(Modifier.fillMaxWidth().padding(vertical = Dimens.xs), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(p.date, style = MaterialTheme.typography.bodySmall)
                        Text("SAR ${fmt.format(p.amount)}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            Spacer(Modifier.height(Dimens.md))
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.sm)) {
                TasheelTextButton("Settlement", onClick = { onSettlement(loan.id) })
                TasheelTextButton("Liability Letter", onClick = { onLiability(loan.id) })
            }
        }
    }
}

@Preview @Composable private fun PreviewDashboard() = TasheelTheme {
    Column(Modifier.padding(Dimens.lg)) { Text("My Dashboard", style = MaterialTheme.typography.headlineSmall) }
}
