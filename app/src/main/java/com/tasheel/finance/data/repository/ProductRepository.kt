package com.tasheel.finance.data.repository

import com.tasheel.finance.data.api.TasheelApi
import com.tasheel.finance.domain.model.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(private val api: TasheelApi) {
    suspend fun getProducts(): List<Product> = api.getProducts().map { it.toDomain() }
    suspend fun getProduct(id: String): Product = api.getProduct(id).toDomain()
}
