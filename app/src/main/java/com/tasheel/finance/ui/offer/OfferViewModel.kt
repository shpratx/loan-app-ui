package com.tasheel.finance.ui.offer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasheel.finance.core.UiState
import com.tasheel.finance.data.repository.LoanRepository
import com.tasheel.finance.domain.model.Offer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferViewModel @Inject constructor(private val repo: LoanRepository) : ViewModel() {
    private val _state = MutableStateFlow<UiState<Offer>>(UiState.Loading)
    val state = _state.asStateFlow()
    private val _accepted = MutableStateFlow(false)
    val accepted = _accepted.asStateFlow()

    fun load(applicationId: String) = viewModelScope.launch {
        _state.value = UiState.Loading
        try { _state.value = UiState.Success(repo.getOffer(applicationId)) }
        catch (e: Exception) { _state.value = UiState.Error(e.message ?: "Failed to load offer") }
    }

    fun accept(offerId: String) = viewModelScope.launch {
        try { repo.acceptOffer(offerId); _accepted.value = true }
        catch (e: Exception) { _state.value = UiState.Error(e.message ?: "Failed to accept offer") }
    }

    fun decline(offerId: String) = viewModelScope.launch {
        try { repo.declineOffer(offerId); _accepted.value = true }
        catch (_: Exception) {}
    }
}
