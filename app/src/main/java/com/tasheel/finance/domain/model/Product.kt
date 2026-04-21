package com.tasheel.finance.domain.model

data class Product(
    val id: String,
    val name: String,
    val type: String,
    val minAmount: Double,
    val maxAmount: Double,
    val minTenureMonths: Int,
    val maxTenureMonths: Int,
    val interestRate: Double,
    val description: String,
)
