package com.tasheel.finance.ui.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasheel.finance.data.repository.ApplicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CardCollectionState(
    val cardNumber: String = "", val expiryMonth: String = "", val expiryYear: String = "",
    val cvv: String = "", val autoDebitDay: String = "1",
    val isSubmitting: Boolean = false, val error: String? = null, val success: Boolean = false,
)

@HiltViewModel
class CardCollectionViewModel @Inject constructor(private val repo: ApplicationRepository) : ViewModel() {
    private val _state = MutableStateFlow(CardCollectionState())
    val state = _state.asStateFlow()

    fun update(fn: CardCollectionState.() -> CardCollectionState) { _state.value = _state.value.fn() }

    fun registerCard(applicationId: String) = viewModelScope.launch {
        _state.value = _state.value.copy(isSubmitting = true, error = null)
        val s = _state.value
        try {
            repo.registerCard(applicationId, s.cardNumber, s.expiryMonth.toInt(), s.expiryYear.toInt(), s.autoDebitDay.toInt())
            _state.value = _state.value.copy(isSubmitting = false, success = true)
        } catch (e: Exception) {
            _state.value = _state.value.copy(isSubmitting = false, error = e.message ?: "Card registration failed")
        }
    }
}
