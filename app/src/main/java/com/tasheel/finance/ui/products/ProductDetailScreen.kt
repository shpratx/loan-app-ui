package com.tasheel.finance.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.tasheel.finance.core.UiState
import com.tasheel.finance.domain.model.Product
import com.tasheel.finance.ui.components.*
import com.tasheel.finance.ui.theme.Dimens
import com.tasheel.finance.ui.theme.TasheelTheme
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(productId: String, onApply: (String) -> Unit, onBack: () -> Unit, viewModel: ProductDetailViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(productId) { viewModel.load(productId) }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Product Details") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } })
    }) { padding ->
        Column(Modifier.padding(padding).padding(Dimens.lg)) {
            when (val s = state) {
                is UiState.Loading -> LoadingSkeletonList(2)
                is UiState.Error -> ErrorState(s.message, onRetry = { viewModel.load(productId) })
                is UiState.Success -> ProductDetailContent(s.data, onApply)
            }
        }
    }
}

@Composable
private fun ProductDetailContent(product: Product, onApply: (String) -> Unit) {
    val fmt = NumberFormat.getNumberInstance(Locale("en", "SA"))
    Text(product.name, style = MaterialTheme.typography.headlineMedium)
    Spacer(Modifier.height(Dimens.lg))
    Text(product.description, style = MaterialTheme.typography.bodyLarge)
    Spacer(Modifier.height(Dimens.xl))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        TasheelStatusCard("Min Amount", "SAR ${fmt.format(product.minAmount)}", Modifier.weight(1f))
        Spacer(Modifier.width(Dimens.sm))
        TasheelStatusCard("Max Amount", "SAR ${fmt.format(product.maxAmount)}", Modifier.weight(1f))
    }
    Spacer(Modifier.height(Dimens.md))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        TasheelStatusCard("Tenure", "${product.minTenureMonths}–${product.maxTenureMonths} mo", Modifier.weight(1f))
        Spacer(Modifier.width(Dimens.sm))
        TasheelStatusCard("Rate", "${product.interestRate}% APR", Modifier.weight(1f))
    }
    Spacer(Modifier.height(Dimens.xl))
    TasheelPrimaryButton("Apply Now", onClick = { onApply(product.id) }, modifier = Modifier.fillMaxWidth())
}

@Preview @Composable private fun PreviewDetail() = TasheelTheme {
    Column(Modifier.padding(Dimens.lg)) { Text("Cash Finance", style = MaterialTheme.typography.headlineMedium) }
}
