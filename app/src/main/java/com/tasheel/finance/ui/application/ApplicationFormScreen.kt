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
import com.tasheel.finance.ui.components.TasheelSecondaryButton
import com.tasheel.finance.ui.components.TasheelTextField
import com.tasheel.finance.ui.theme.Dimens
import com.tasheel.finance.ui.theme.TasheelTheme

private val stepTitles = listOf("Personal Info", "Employment", "Income & Amount", "Review")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationFormScreen(
    productId: String, onApplicationCreated: (String) -> Unit, onBack: () -> Unit,
    viewModel: ApplicationFormViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state.createdAppId) { state.createdAppId?.let { onApplicationCreated(it) } }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Apply – ${stepTitles[state.step]}") },
            navigationIcon = { IconButton(onClick = { if (state.step > 0) viewModel.prevStep() else onBack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } })
    }) { padding ->
        Column(Modifier.padding(padding).padding(Dimens.lg).fillMaxSize()) {
            LinearProgressIndicator(progress = { (state.step + 1) / 4f }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(Dimens.xl))
            when (state.step) {
                0 -> PersonalInfoStep(state, viewModel::update)
                1 -> EmploymentStep(state, viewModel::update)
                2 -> IncomeStep(state, viewModel::update)
                3 -> ReviewStep(state)
            }
            Spacer(Modifier.weight(1f))
            state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall); Spacer(Modifier.height(Dimens.sm)) }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Dimens.md)) {
                if (state.step < 3) {
                    TasheelSecondaryButton("Save Draft", onClick = { viewModel.saveDraft(productId) }, modifier = Modifier.weight(1f))
                    TasheelPrimaryButton("Next", onClick = viewModel::nextStep, modifier = Modifier.weight(1f))
                } else {
                    TasheelPrimaryButton(
                        text = if (state.isSubmitting) "Submitting..." else "Submit Application",
                        onClick = { viewModel.submit(productId) }, modifier = Modifier.fillMaxWidth(), enabled = !state.isSubmitting,
                    )
                }
            }
        }
    }
}

@Composable private fun PersonalInfoStep(s: ApplicationFormState, update: ((ApplicationFormState.() -> ApplicationFormState) -> Unit)) {
    TasheelTextField(s.fullName, { update { copy(fullName = it) } }, "Full Name (Arabic)")
    Spacer(Modifier.height(Dimens.lg))
    TasheelTextField(s.nationalId, { update { copy(nationalId = it) } }, "National ID / Iqama")
}

@Composable private fun EmploymentStep(s: ApplicationFormState, update: ((ApplicationFormState.() -> ApplicationFormState) -> Unit)) {
    TasheelTextField(s.employer, { update { copy(employer = it) } }, "Employer Name")
}

@Composable private fun IncomeStep(s: ApplicationFormState, update: ((ApplicationFormState.() -> ApplicationFormState) -> Unit)) {
    TasheelTextField(s.monthlySalary, { update { copy(monthlySalary = it) } }, "Monthly Salary (SAR)")
    Spacer(Modifier.height(Dimens.lg))
    TasheelTextField(s.requestedAmount, { update { copy(requestedAmount = it) } }, "Requested Amount (SAR)")
    Spacer(Modifier.height(Dimens.lg))
    TasheelTextField(s.requestedTenure, { update { copy(requestedTenure = it) } }, "Tenure (months)")
}

@Composable private fun ReviewStep(s: ApplicationFormState) {
    listOf("Name" to s.fullName, "National ID" to s.nationalId, "Employer" to s.employer,
        "Salary" to "SAR ${s.monthlySalary}", "Amount" to "SAR ${s.requestedAmount}", "Tenure" to "${s.requestedTenure} months",
    ).forEach { (label, value) ->
        Row(Modifier.fillMaxWidth().padding(vertical = Dimens.xs), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview @Composable private fun PreviewForm() = TasheelTheme {
    Column(Modifier.padding(Dimens.lg)) {
        Text("Apply – Personal Info", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(Dimens.lg))
        TasheelTextField("", {}, "Full Name (Arabic)")
    }
}
