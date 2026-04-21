package com.tasheel.finance.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.tasheel.finance.ui.components.TasheelPrimaryButton
import com.tasheel.finance.ui.components.TasheelTextField
import com.tasheel.finance.ui.theme.Dimens
import com.tasheel.finance.ui.theme.TasheelTheme

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, viewModel: LoginViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state.success) { if (state.success) onLoginSuccess() }

    Column(
        Modifier.fillMaxSize().padding(Dimens.xl),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Tasheel Finance", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(Dimens.xxl))
        TasheelTextField(state.username, viewModel::onUsernameChange, "Username")
        Spacer(Modifier.height(Dimens.lg))
        OutlinedTextField(
            value = state.password, onValueChange = viewModel::onPasswordChange,
            label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(), singleLine = true,
        )
        state.error?.let {
            Spacer(Modifier.height(Dimens.sm))
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(Modifier.height(Dimens.xl))
        TasheelPrimaryButton(
            text = if (state.isLoading) "Signing in..." else "Sign In",
            onClick = viewModel::login, modifier = Modifier.fillMaxWidth(), enabled = !state.isLoading,
        )
    }
}

@Preview @Composable private fun PreviewLogin() = TasheelTheme {
    Column(Modifier.fillMaxSize().padding(Dimens.xl), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Tasheel Finance", style = MaterialTheme.typography.headlineLarge)
    }
}
