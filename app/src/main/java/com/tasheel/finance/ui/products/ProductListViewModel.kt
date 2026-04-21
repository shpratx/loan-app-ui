package com.tasheel.finance.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasheel.finance.core.UiState
import com.tasheel.finance.domain.model.Product
import com.tasheel.finance.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(private val getProducts: GetProductsUseCase) : ViewModel() {
    private val _state = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val state = _state.asStateFlow()

    init { load() }

    fun load() = viewModelScope.launch {
        _state.value = UiState.Loading
        try { _state.value = UiState.Success(getProducts()) }
        catch (e: Exception) { _state.value = UiState.Error(e.message ?: "Failed to load products") }
    }
}
