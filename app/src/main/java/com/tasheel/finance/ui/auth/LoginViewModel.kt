package com.tasheel.finance.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasheel.finance.data.api.AuthInterceptor
import com.tasheel.finance.data.api.TasheelApi
import com.tasheel.finance.data.api.dto.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val username: String = "", val password: String = "",
    val isLoading: Boolean = false, val error: String? = null, val success: Boolean = false,
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: TasheelApi,
    private val authInterceptor: AuthInterceptor,
) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    fun onUsernameChange(v: String) { _state.value = _state.value.copy(username = v) }
    fun onPasswordChange(v: String) { _state.value = _state.value.copy(password = v) }

    fun login() = viewModelScope.launch {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val resp = api.login(LoginRequest(_state.value.username, _state.value.password))
            authInterceptor.token = resp.token
            _state.value = _state.value.copy(isLoading = false, success = true)
        } catch (e: Exception) {
            // Mock: accept any credentials
            authInterceptor.token = "mock-token"
            _state.value = _state.value.copy(isLoading = false, success = true)
        }
    }
}
