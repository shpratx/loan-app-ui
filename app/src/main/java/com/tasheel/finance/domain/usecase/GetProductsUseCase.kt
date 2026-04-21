package com.tasheel.finance.domain.usecase

import com.tasheel.finance.data.repository.ProductRepository
import com.tasheel.finance.domain.model.Product
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val repo: ProductRepository) {
    suspend operator fun invoke(): List<Product> = repo.getProducts()
}
