package com.tasheel.finance.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
fun ProductListScreen(
    onProductClick: (String) -> Unit, onDashboardClick: () -> Unit,
    viewModel: ProductListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Tasheel Finance") },
            actions = { IconButton(onClick = onDashboardClick) { Icon(Icons.Default.AccountCircle, "Dashboard") } },
        )
    }) { padding ->
        Column(Modifier.padding(padding).padding(Dimens.lg)) {
            Text("Our Products", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(Dimens.lg))
            when (val s = state) {
                is UiState.Loading -> LoadingSkeletonList()
                is UiState.Error -> ErrorState(s.message, onRetry = viewModel::load)
                is UiState.Success -> Column(verticalArrangement = Arrangement.spacedBy(Dimens.md)) {
                    s.data.forEach { product ->
                        val fmt = NumberFormat.getNumberInstance(Locale("en", "SA"))
                        TasheelProductCard(
                            title = product.name,
                            description = "SAR ${fmt.format(product.minAmount)} – ${fmt.format(product.maxAmount)} • ${product.minTenureMonths}–${product.maxTenureMonths} months",
                            onClick = { onProductClick(product.id) },
                        ) { Text("From ${product.interestRate}% APR", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary) }
                    }
                }
            }
        }
    }
}

@Preview @Composable private fun PreviewProductList() = TasheelTheme {
    Column(Modifier.padding(Dimens.lg)) {
        Text("Our Products", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(Dimens.lg))
        TasheelProductCard("Cash Finance", "SAR 5,000 – 250,000 • 3–60 months")
        Spacer(Modifier.height(Dimens.md))
        TasheelProductCard("Combo Finance", "SAR 10,000 – 500,000 • 6–60 months")
    }
}
