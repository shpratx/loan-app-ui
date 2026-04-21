package com.tasheel.finance.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasheel.finance.core.UiState
import com.tasheel.finance.data.repository.LoanRepository
import com.tasheel.finance.domain.model.Loan
import com.tasheel.finance.domain.model.Payment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardData(val loans: List<Loan>, val payments: Map<String, List<Payment>> = emptyMap(), val settlementAmount: Double? = null)

@HiltViewModel
class DashboardViewModel @Inject constructor(private val repo: LoanRepository) : ViewModel() {
    private val _state = MutableStateFlow<UiState<DashboardData>>(UiState.Loading)
    val state = _state.asStateFlow()

    init { load() }

    fun load() = viewModelScope.launch {
        _state.value = UiState.Loading
        try {
            val loans = repo.getLoans()
            val payments = loans.associate { it.id to repo.getPayments(it.id) }
            _state.value = UiState.Success(DashboardData(loans, payments))
        } catch (e: Exception) { _state.value = UiState.Error(e.message ?: "Failed to load dashboard") }
    }

    fun requestSettlement(loanId: String) = viewModelScope.launch {
        try {
            val s = repo.getSettlement(loanId)
            val current = (_state.value as? UiState.Success)?.data ?: return@launch
            _state.value = UiState.Success(current.copy(settlementAmount = s.settlementAmount))
        } catch (_: Exception) {}
    }

    fun requestLiabilityLetter(loanId: String) = viewModelScope.launch {
        try { repo.requestLiabilityLetter(loanId) } catch (_: Exception) {}
    }
}
