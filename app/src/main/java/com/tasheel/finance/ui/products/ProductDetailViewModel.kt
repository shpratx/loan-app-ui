package com.tasheel.finance.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasheel.finance.core.UiState
import com.tasheel.finance.data.repository.ProductRepository
import com.tasheel.finance.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(private val repo: ProductRepository) : ViewModel() {
    private val _state = MutableStateFlow<UiState<Product>>(UiState.Loading)
    val state = _state.asStateFlow()

    fun load(id: String) = viewModelScope.launch {
        _state.value = UiState.Loading
        try { _state.value = UiState.Success(repo.getProduct(id)) }
        catch (e: Exception) { _state.value = UiState.Error(e.message ?: "Failed to load product") }
    }
}
