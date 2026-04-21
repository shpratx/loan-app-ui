package com.tasheel.finance.ui.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasheel.finance.data.db.ApplicationDraftEntity
import com.tasheel.finance.data.repository.ApplicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ApplicationFormState(
    val step: Int = 0, // 0=personal, 1=employment, 2=income, 3=review
    val fullName: String = "", val nationalId: String = "",
    val employer: String = "", val monthlySalary: String = "",
    val requestedAmount: String = "", val requestedTenure: String = "",
    val isSubmitting: Boolean = false, val error: String? = null, val createdAppId: String? = null,
)

@HiltViewModel
class ApplicationFormViewModel @Inject constructor(private val repo: ApplicationRepository) : ViewModel() {
    private val _state = MutableStateFlow(ApplicationFormState())
    val state = _state.asStateFlow()

    fun update(fn: ApplicationFormState.() -> ApplicationFormState) { _state.value = _state.value.fn() }
    fun nextStep() { _state.value = _state.value.copy(step = _state.value.step + 1) }
    fun prevStep() { _state.value = _state.value.copy(step = (_state.value.step - 1).coerceAtLeast(0)) }

    fun saveDraft(productId: String) = viewModelScope.launch {
        val s = _state.value
        repo.saveDraft(ApplicationDraftEntity(
            productId = productId, fullName = s.fullName, nationalId = s.nationalId,
            employer = s.employer, monthlySalary = s.monthlySalary.toDoubleOrNull() ?: 0.0,
            requestedAmount = s.requestedAmount.toDoubleOrNull() ?: 0.0,
            requestedTenure = s.requestedTenure.toIntOrNull() ?: 0, currentStep = s.step,
        ))
    }

    fun submit(productId: String) = viewModelScope.launch {
        _state.value = _state.value.copy(isSubmitting = true, error = null)
        val s = _state.value
        try {
            val app = repo.createApplication(productId, s.requestedAmount.toDouble(), s.requestedTenure.toInt(),
                s.fullName, s.nationalId, s.employer, s.monthlySalary.toDouble())
            repo.submitApplication(app.id)
            _state.value = _state.value.copy(isSubmitting = false, createdAppId = app.id)
        } catch (e: Exception) {
            _state.value = _state.value.copy(isSubmitting = false, error = e.message ?: "Submission failed")
        }
    }
}
